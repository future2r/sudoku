package name.ulbricht.sudoku;

import java.io.IOException;

/**
 * Represents a Sudoku grid with 9 by 9 cells.
 */
public final class Grid {

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
	public static Grid empty() {
		return new Grid();
	}

	/**
	 * Creates a new grid from the specified pattern. Non-empty cells from the
	 * pattern are filled into the grid but not locked.
	 * 
	 * @param pattern the pattern describing the grid
	 * @return a new grid
	 * @see GridFile
	 */
	public static Grid of(final String pattern) {
		return of(pattern, false);
	}

	/**
	 * Creates a new grid from the specified pattern. Non-empty cells from the
	 * pattern are filled into the grid and locked.
	 * 
	 * @param pattern the pattern describing the grid
	 * @return a new grid with locked cells
	 * @see GridFile
	 */
	public static Grid ofLocked(final String pattern) {
		return of(pattern, true);
	}

	private static Grid of(final String s, final boolean locked) {
		try {
			return GridFile.parse(s, locked);
		} catch (final IOException e) {
			throw new IllegalArgumentException("Invalid grid definition", e);
		}
	}

	private final int[] cells = new int[GRID_SIZE * GRID_SIZE];

	private Grid() {
		// hidden
	}

	/**
	 * Returns the value of the specified cell.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @return the current value of the cell, will be {@link #EMPTY_VALUE} if the
	 *         cell is empty
	 * @throws IndexOutOfBoundsException if the column or the row is invalid
	 * @see #set(int, int, int)
	 * @see #empty(int, int)
	 */
	public int get(final int column, final int row) throws IndexOutOfBoundsException {
		return Math.abs(getRaw(validColumn(column), validRow(row)));
	}

	/**
	 * Sets a new value to the specified cell. The new value will replace the old
	 * value. If the cell is locked the value cannot be changed. Setting a cell to
	 * {@link #EMPTY_VALUE} will empty the cell.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @param value  the new value of the cell or {@link #EMPTY_VALUE}
	 * @throws IndexOutOfBoundsException if the column or the row is invalid
	 * @throws IllegalArgumentException  if the value is invalid
	 * @throws RuleViolationException    if the new value cannot be set by Sudoku
	 *                                   rules (e.g. already exsiting or locked)
	 * @see #get(int, int)
	 */
	public void set(final int column, final int row, final int value)
			throws IndexOutOfBoundsException, IllegalArgumentException, RuleViolationException {
		if (locked(column, row))
			throw new RuleViolationException("Cannot change a locked cell");
		validateRules(column, row, validValue(value));
		setRaw(column, row, value);
	}

	/**
	 * Checks if the specified cell is empty.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @return {@code true} if the cells is empty, otherwise {@code false}
	 * @throws IndexOutOfBoundsException if the column or the row is invalid
	 * @see #get(int, int)
	 */
	public boolean empty(final int column, final int row) throws IndexOutOfBoundsException {
		return get(column, row) == EMPTY_VALUE;
	}

	/**
	 * Clears the specified cell by setting its value to {@link #EMPTY_VALUE}.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @throws IndexOutOfBoundsException if the column or the row is invalid
	 * @throws RuleViolationException    if the new value cannot be set by Sudoku
	 *                                   rules (e.g. locked)
	 * @see #empty(int, int)
	 */
	public void clear(final int column, final int row) throws IndexOutOfBoundsException, RuleViolationException {
		set(column, row, EMPTY_VALUE);
	}

	/**
	 * Locks the specified cell and sets a new value. Locked cells cannot be empty.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @param value  the new value of the cell, but not {@link #EMPTY_VALUE}
	 * @throws IndexOutOfBoundsException if the column or the row is invalid
	 * @throws IllegalArgumentException  if the value is invalid
	 * @throws RuleViolationException    if the value cannot be set by Sudoku rules
	 *                                   (e.g. already existing or empty)
	 * @see #unlock(int, int)
	 */
	public void lock(final int column, final int row, final int value)
			throws IndexOutOfBoundsException, IllegalArgumentException, RuleViolationException {
		if (validValue(value) == EMPTY_VALUE)
			throw new IllegalArgumentException("Cannot lock empty cell");
		validateRules(validColumn(column), validRow(row), value);
		setRaw(column, row, -value);
	}

	/**
	 * Checks if the specified cell is locked. Locked cells cannot be changed and
	 * are never empty.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @return {@code true} if the cells is locked, otherwise {@code false}
	 * @throws IndexOutOfBoundsException if the column or the row is invalid
	 * @see #lock(int, int, int)
	 */
	public boolean locked(final int column, final int row) throws IndexOutOfBoundsException {
		return getRaw(validColumn(column), validRow(row)) < 0;
	}

	/**
	 * Unlocks the specified cell. The current cell value remains unchanged.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @throws IndexOutOfBoundsException if the column or the row is invalid
	 * @see #lock(int, int, int)
	 */
	public void unlock(final int column, final int row) throws IndexOutOfBoundsException {
		if (locked(column, row))
			setRaw(column, row, Math.abs(getRaw(column, row)));
	}

	/**
	 * Returns the current value of a cell from the internal data structure. The
	 * column and row must be validated before calling this method. If the cell is
	 * locked the value is negative.
	 * 
	 * @param column the column of the cell
	 * @param row    the row of the cell
	 * @return the current value of the cell, or a negative value if the cell is
	 *         locked
	 */
	private int getRaw(final int column, final int row) {
		return this.cells[toIndex(column, row)];
	}

	/**
	 * Sets the specified value to the internal data structure. The column, row and
	 * value must be validated before calling this method. No rules are checked. If
	 * the value is negative the cell will be locked.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @param value  the new value of the cell
	 */
	private void setRaw(final int column, final int row, final int value) {
		this.cells[toIndex(column, row)] = value;
	}

	/**
	 * Converts the specified column and row to a linear index for accessing the
	 * internal data structure.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @return the linear index
	 */
	private static int toIndex(final int column, final int row) {
		return ((column - 1) * GRID_SIZE) + (row - 1);
	}

	/**
	 * Validates if the specified column is valid and returns it.
	 * 
	 * @param column the column of the cell (one-based)
	 * @return a valid column
	 * @throws IndexOutOfBoundsException if the column is invalid
	 */
	private static int validColumn(final int column) throws IndexOutOfBoundsException {
		if (column < 1 || column > GRID_SIZE)
			throw new IndexOutOfBoundsException("Invalid column: " + column);
		return column;
	}

	/**
	 * Validates if the specified row is valid.
	 * 
	 * @param row the row of the cell (one-based)
	 * @return a valid row
	 * @throws IndexOutOfBoundsException if the row is invalid
	 */
	private static int validRow(final int row) throws IndexOutOfBoundsException {
		if (row < 1 || row > GRID_SIZE)
			throw new IndexOutOfBoundsException("Invalid row: " + row);
		return row;
	}

	/**
	 * Checks if the specified value is a valid value or {@link #EMPTY_VALUE}.
	 * 
	 * @param value the value to validate
	 * @return a valid value
	 * @throws IllegalArgumentException if the value is invalid
	 */
	private static int validValue(final int value) throws IllegalArgumentException {
		if (value != EMPTY_VALUE && (value < MIN_VALUE || value > MAX_VALUE))
			throw new IllegalArgumentException("Invalid value: " + value);
		return value;
	}

	/**
	 * Check if the new value for a cell is allowed according to the Sudoku rules:
	 * <ol>
	 * <li>A value may exist only once in its box</li>
	 * <li>A value may exist only once in its column</li>
	 * <li>A value may exist only once in its row</li>
	 * </ol>
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @param value  the value to check
	 * @throws RuleViolationException if the value would violate one of the rules
	 */
	private void validateRules(final int column, final int row, final int value) throws RuleViolationException {
		if (value != EMPTY_VALUE) {
			if (existsInBox(column, row, value))
				throw new RuleViolationException(String.format("value %d already exists in box", value));
			if (existsInColumn(column, row, value))
				throw new RuleViolationException(String.format("value %d already exists in column %d", value, column));
			if (existsInRow(column, row, value))
				throw new RuleViolationException(String.format("value %d already exists in row %d", value, row));
		}
	}

	/**
	 * Checks if the specified value already exsists in the current box. The column,
	 * row and value must be validated before calling this method. The specified
	 * cell is ignored.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @param value  the value to find
	 * @return {@code true} if the value exists, otherwise {@code false}
	 */
	private boolean existsInBox(final int column, final int row, final int value) {
		final var startColumn = boxStart(column);
		final var startRow = boxStart(row);
		for (var c = startColumn; c < (startColumn + BOX_SIZE); c++) {
			for (var r = startRow; r < (startRow + BOX_SIZE); r++) {
				if ((c != column || r != row) && get(c, r) == value)
					return true;
			}
		}
		return false;
	}

	/**
	 * Converts a column or row into the first column or row of the current box.
	 * 
	 * @param index a column or row index
	 * @return the first column or first row of the current box
	 */
	private static int boxStart(final int index) {
		return ((index - 1) / BOX_SIZE) * BOX_SIZE + 1;
	}

	/**
	 * Checks if the specified value already exsists in the current column. The
	 * column, row and value must be validated before calling this method. The
	 * specified cell is ignored.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @param value  the value to find
	 * @return {@code true} if the value exists, otherwise {@code false}
	 */
	private boolean existsInColumn(final int column, final int row, final int value) {
		for (var r = 1; r <= GRID_SIZE; r++) {
			if (r != row && get(column, r) == value)
				return true;
		}
		return false;
	}

	/**
	 * Checks if the specified value already exsists in the current row. The column,
	 * row and value must be validated before calling this method. The specified
	 * cell is ignored.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @param value  the value to find
	 * @return {@code true} if the value exists, otherwise {@code false}
	 */
	private boolean existsInRow(final int column, final int row, final int value) {
		for (var c = 1; c <= GRID_SIZE; c++) {
			if (c != column && get(c, row) == value)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		final var line = "-".repeat(GRID_SIZE * 3 + (GRID_SIZE / BOX_SIZE) + 1) + '\n';
		final var sb = new StringBuilder(line);

		for (var row = 1; row <= GRID_SIZE; row++) {
			sb.append('|');
			for (var column = 1; column <= GRID_SIZE; column++) {
				final var value = getRaw(column, row);
				sb.append(value < 0 ? '<' : ' ');
				sb.append(value != EMPTY_VALUE ? (char) (Math.abs(value) + 0x30) : '.');
				sb.append(value < 0 ? '>' : ' ');
				if (column % BOX_SIZE == 0)
					sb.append('|');
			}
			sb.append('\n');
			if (row % BOX_SIZE == 0)
				sb.append(line);
		}

		return sb.toString();
	}
}