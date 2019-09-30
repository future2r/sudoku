package name.ulbricht.sudoku;

import static java.lang.Math.abs;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import name.ulbricht.sudoku.grid.Column;
import name.ulbricht.sudoku.grid.Grid;
import name.ulbricht.sudoku.grid.Row;
import name.ulbricht.sudoku.grid.Section;

/**
 * Represents a Sudoku grid with 9 by 9 cells.
 */
public final class SudokuGrid {

	/**
	 * Value for an empty cell.
	 * 
	 * @see #empty(int, int)
	 * @see #clear(int, int)
	 */
	public static final int EMPTY_VALUE = 0;

	/**
	 * Smallest valid value for a cell (in addition to {@link #EMPTY_VALUE}).
	 */
	public static final int MIN_VALUE = 1;

	/**
	 * Largest valid value for a cell (in addition to {@link #EMPTY_VALUE}).
	 */
	public static final int MAX_VALUE = 9;

	/**
	 * Width and height of the grid.
	 */
	public static final int GRID_SIZE = 9;

	private static final int BOX_SIZE = 3;

	/**
	 * Creates a new grid with all cells empty.
	 * 
	 * @return a new empty grid
	 */
	public static SudokuGrid empty() {
		return new SudokuGrid(Grid.of(GRID_SIZE, GRID_SIZE));
	}

	/**
	 * Creates a new grid from the specified pattern. Non-empty cells from the
	 * pattern are filled into the grid but not locked.
	 * 
	 * @param pattern the pattern describing the grid
	 * @return a new grid
	 * @see SudokuFile
	 */
	public static SudokuGrid of(final String pattern) {
		return of(pattern, false);
	}

	/**
	 * Creates a new grid from the specified pattern. Non-empty cells from the
	 * pattern are filled into the grid and locked.
	 * 
	 * @param pattern the pattern describing the grid
	 * @return a new grid with locked cells
	 * @see SudokuFile
	 */
	public static SudokuGrid ofLocked(final String pattern) {
		return of(pattern, true);
	}

	private static SudokuGrid of(final String s, final boolean locked) {
		try {
			return SudokuFile.parse(s, locked);
		} catch (final IOException e) {
			throw new IllegalArgumentException("Invalid grid definition", e);
		}
	}

	/**
	 * Creates an independent copy the specified Sudoku grid.
	 * 
	 * @param original the original to copy from
	 * @return an independent copy
	 */
	public static SudokuGrid copyOf(final SudokuGrid original) {
		return new SudokuGrid(Grid.copyOf(original.grid));
	}

	private final Grid grid;

	private SudokuGrid(final Grid grid) {
		this.grid = grid;
	}

	/**
	 * Returns the value of the specified cell.
	 * 
	 * @param columnIndex the column of the cell (one-based)
	 * @param rowIndex    the row of the cell (one-based)
	 * @return the current value of the cell, will be {@link #EMPTY_VALUE} if the
	 *         cell is empty
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 * @see #set(int, int, int)
	 * @see #empty(int, int)
	 */
	public int get(final int columnIndex, final int rowIndex) throws IndexOutOfBoundsException {
		return abs(grid.get(columnIndex, rowIndex));
	}

	/**
	 * Sets a new value to the specified cell. The new value will replace the old
	 * value. If the cell is locked the value cannot be changed. Setting a cell to
	 * {@link #EMPTY_VALUE} will empty the cell.
	 * 
	 * @param columnIndex the column of the cell (one-based)
	 * @param rowIndex    the row of the cell (one-based)
	 * @param newValue    the new value of the cell or {@link #EMPTY_VALUE}
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 * @throws IllegalArgumentException  if the value is invalid
	 * @throws RuleViolationException    if the new value cannot be set by Sudoku
	 *                                   rules (e.g. already exsiting or locked)
	 * @see #get(int, int)
	 */
	public void set(final int columnIndex, final int rowIndex, final int newValue)
			throws IndexOutOfBoundsException, IllegalArgumentException, RuleViolationException {
		final var currentValue = grid.get(columnIndex, rowIndex);
		if (currentValue < 0)
			throw new RuleViolationException("Cannot change a locked cell");
		if (currentValue != newValue) {
			validateRules(columnIndex, rowIndex, validValue(newValue));
			this.grid.set(columnIndex, rowIndex, newValue);
		}
	}

	/**
	 * Checks if the specified cell is empty.
	 * 
	 * @param columnIndex the column index of the cell (one-based)
	 * @param rowIndex    the row index of the cell (one-based)
	 * @return {@code true} if the cells is empty, otherwise {@code false}
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 * @see #get(int, int)
	 */
	public boolean empty(final int columnIndex, final int rowIndex) throws IndexOutOfBoundsException {
		return get(columnIndex, rowIndex) == EMPTY_VALUE;
	}

	/**
	 * Clears the specified cell by setting its value to {@link #EMPTY_VALUE}.
	 * 
	 * @param columnIndex the column index of the cell (one-based)
	 * @param rowIndex    the row index of the cell (one-based)
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 * @throws RuleViolationException    if the new value cannot be set by Sudoku
	 *                                   rules (e.g. locked)
	 * @see #empty(int, int)
	 */
	public void clear(final int columnIndex, final int rowIndex)
			throws IndexOutOfBoundsException, RuleViolationException {
		set(columnIndex, rowIndex, EMPTY_VALUE);
	}

	/**
	 * Locks the specified cell and sets a new value. Locked cells cannot be empty.
	 * 
	 * @param columnIndex the column index of the cell (one-based)
	 * @param rowIndex    the row index of the cell (one-based)
	 * @param newValue    the new value of the cell, but not {@link #EMPTY_VALUE}
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 * @throws IllegalArgumentException  if the value is invalid
	 * @throws RuleViolationException    if the value cannot be set by Sudoku rules
	 *                                   (e.g. already existing or empty)
	 * @see #unlock(int, int)
	 */
	public void lock(final int columnIndex, final int rowIndex, final int newValue)
			throws IndexOutOfBoundsException, IllegalArgumentException, RuleViolationException {
		if (validValue(newValue) == EMPTY_VALUE)
			throw new IllegalArgumentException("Cannot lock empty cell");
		final var currentValue = grid.get(columnIndex, rowIndex);
		if (abs(currentValue) != newValue)
			validateRules(columnIndex, rowIndex, newValue);
		this.grid.set(columnIndex, rowIndex, -newValue);
	}

	/**
	 * Checks if the specified cell is locked. Locked cells cannot be changed and
	 * are never empty.
	 * 
	 * @param columnIndex the column index of the cell (one-based)
	 * @param rowIndex    the row index of the cell (one-based)
	 * @return {@code true} if the cells is locked, otherwise {@code false}
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 * @see #lock(int, int, int)
	 */
	public boolean locked(final int columnIndex, final int rowIndex) throws IndexOutOfBoundsException {
		return grid.get(columnIndex, rowIndex) < 0;
	}

	/**
	 * Unlocks the specified cell. The current cell value remains unchanged.
	 * 
	 * @param columnIndex the column index of the cell (one-based)
	 * @param rowIndex    the row index of the cell (one-based)
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 * @see #lock(int, int, int)
	 */
	public void unlock(final int columnIndex, final int rowIndex) throws IndexOutOfBoundsException {
		if (locked(columnIndex, rowIndex))
			this.grid.set(columnIndex, rowIndex, abs(this.grid.get(columnIndex, rowIndex)));
	}

	/**
	 * An empty array, meaning no value is accepted.
	 */
	private static final int[] NOTHING_ACCEPTED = {};

	/**
	 * Returns all values that will be candidates for this cell by the Sudoku rules.
	 * The candidates depend on the values of other cells in the column, row
	 * and box. If the cell already has a value, an empty array is returned. The
	 * returned array contains only unique accepted values without
	 * {@link #EMPTY_VALUE}. For an empty cell at least one value will be accepted.
	 * 
	 * @param columnIndex the column index of the cell (one-based)
	 * @param rowIndex    the row index of the cell (one-based)
	 * @return an array with candidates
	 * @throws IndexOutOfBoundsException if the column index or the row index is
	 *                                   invalid
	 */
	public int[] candidates(final int columnIndex, final int rowIndex) throws IndexOutOfBoundsException {
		if (!empty(columnIndex, rowIndex))
			return NOTHING_ACCEPTED;

		final var candidates = new int[MAX_VALUE - MIN_VALUE + 1];
		var candidatesdIndex = 0;

		final var box = this.grid.section(boxStart(columnIndex), boxStart(rowIndex), BOX_SIZE, BOX_SIZE);
		final var column = this.grid.column(columnIndex);
		final var row = this.grid.row(rowIndex);

		for (var value = MIN_VALUE; value <= MAX_VALUE; value++) {

			if (!(existsInBox(box, value) || existsInColumn(column, value) || existsInRow(row, value))) {
				candidates[candidatesdIndex] = value;
				candidatesdIndex++;
			}
		}
		return Arrays.copyOf(candidates, candidatesdIndex);
	}

	private static int validValue(final int value) throws IllegalArgumentException {
		if (value != EMPTY_VALUE && (value < MIN_VALUE || value > MAX_VALUE))
			throw new IllegalArgumentException("Invalid value: " + value);
		return value;
	}

	private void validateRules(final int columnIndex, final int rowIndex, final int newValue)
			throws RuleViolationException {
		if (newValue != EMPTY_VALUE) {
			if (existsInBox(grid.section(boxStart(columnIndex), boxStart(rowIndex), BOX_SIZE, BOX_SIZE), newValue))
				throw new RuleViolationException(String.format("value %d already exists in box", newValue));
			if (existsInColumn(this.grid.column(columnIndex), newValue))
				throw new RuleViolationException(
						String.format("value %d already exists in column %d", newValue, columnIndex));
			if (existsInRow(this.grid.row(rowIndex), newValue))
				throw new RuleViolationException(
						String.format("value %d already exists in row %d", newValue, rowIndex));
		}
	}

	private boolean existsInBox(final Section box, final int value) {
		for (var column = 1; column <= box.columns(); column++) {
			for (var row = 1; row <= box.rows(); row++) {
				if (abs(box.get(column, row)) == value)
					return true;
			}
		}
		return false;
	}

	private boolean existsInColumn(final Column column, final int value) {
		for (var row = 1; row <= column.rows(); row++) {
			if (abs(column.get(row)) == value)
				return true;
		}
		return false;
	}

	private boolean existsInRow(final Row row, final int value) {
		for (var column = 1; column <= row.columns(); column++) {
			if (abs(row.get(column)) == value)
				return true;
		}
		return false;
	}

	private static int boxStart(final int index) {
		return ((index - 1) / BOX_SIZE) * BOX_SIZE + 1;
	}

	@Override
	public int hashCode() {
		return this.grid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		final var other = (SudokuGrid) obj;
		return Objects.equals(this.grid, other.grid);
	}

	@Override
	public String toString() {
		final var line = "-".repeat(GRID_SIZE * 3 + (GRID_SIZE / BOX_SIZE) + 1) + '\n';
		final var sb = new StringBuilder(line);

		for (var rowIndex = 1; rowIndex <= GRID_SIZE; rowIndex++) {
			sb.append('|');
			for (var columnIndex = 1; columnIndex <= GRID_SIZE; columnIndex++) {
				final var value = grid.get(columnIndex, rowIndex);
				sb.append(value < 0 ? '<' : ' ');
				sb.append(value != EMPTY_VALUE ? (char) (abs(value) + 0x30) : '.');
				sb.append(value < 0 ? '>' : ' ');
				if (columnIndex % BOX_SIZE == 0)
					sb.append('|');
			}
			sb.append('\n');
			if (rowIndex % BOX_SIZE == 0)
				sb.append(line);
		}

		return sb.toString();
	}
}