package name.ulbricht.sudoku;

import static java.lang.Math.abs;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents a Sudoku grid with 9 by 9 cells.
 */
public final class Grid {

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

	/**
	 * Creates an independent copy the specified Sudoku grid.
	 * 
	 * @param original the original to copy from
	 * @return an independent copy
	 */
	public static Grid copyOf(final Grid original) {
		return new Grid(original);
	}

	/**
	 * This array contains the indices of the cells a cell depends on. This is
	 * pre-defined by performance reasons. Each cell depends on 8 cells in its box,
	 * 6 cell in its row (outside of the box) and 6 cells in its column (outside of
	 * the box). In the end, each cell has 20 dependencies. Therefore, this array
	 * has 1.6020 entries (20 * 81).
	 */
	private static int[] dependencies = { //
			1, 2, 9, 10, 11, 18, 19, 20, 3, 4, 5, 6, 7, 8, 27, 36, 45, 54, 63, 72, // 0
			0, 2, 9, 10, 11, 18, 19, 20, 3, 4, 5, 6, 7, 8, 28, 37, 46, 55, 64, 73, // 1
			0, 1, 9, 10, 11, 18, 19, 20, 3, 4, 5, 6, 7, 8, 29, 38, 47, 56, 65, 74, // 2
			4, 5, 12, 13, 14, 21, 22, 23, 0, 1, 2, 6, 7, 8, 30, 39, 48, 57, 66, 75, // 3
			3, 5, 12, 13, 14, 21, 22, 23, 0, 1, 2, 6, 7, 8, 31, 40, 49, 58, 67, 76, // 4
			3, 4, 12, 13, 14, 21, 22, 23, 0, 1, 2, 6, 7, 8, 32, 41, 50, 59, 68, 77, // 5
			7, 8, 15, 16, 17, 24, 25, 26, 0, 1, 2, 3, 4, 5, 33, 42, 51, 60, 69, 78, // 6
			6, 8, 15, 16, 17, 24, 25, 26, 0, 1, 2, 3, 4, 5, 34, 43, 52, 61, 70, 79, // 7
			6, 7, 15, 16, 17, 24, 25, 26, 0, 1, 2, 3, 4, 5, 35, 44, 53, 62, 71, 80, // 8
			0, 1, 2, 10, 11, 18, 19, 20, 12, 13, 14, 15, 16, 17, 27, 36, 45, 54, 63, 72, // 9
			0, 1, 2, 9, 11, 18, 19, 20, 12, 13, 14, 15, 16, 17, 28, 37, 46, 55, 64, 73, // 10
			0, 1, 2, 9, 10, 18, 19, 20, 12, 13, 14, 15, 16, 17, 29, 38, 47, 56, 65, 74, // 11
			3, 4, 5, 13, 14, 21, 22, 23, 9, 10, 11, 15, 16, 17, 30, 39, 48, 57, 66, 75, // 12
			3, 4, 5, 12, 14, 21, 22, 23, 9, 10, 11, 15, 16, 17, 31, 40, 49, 58, 67, 76, // 13
			3, 4, 5, 12, 13, 21, 22, 23, 9, 10, 11, 15, 16, 17, 32, 41, 50, 59, 68, 77, // 14
			6, 7, 8, 16, 17, 24, 25, 26, 9, 10, 11, 12, 13, 14, 33, 42, 51, 60, 69, 78, // 15
			6, 7, 8, 15, 17, 24, 25, 26, 9, 10, 11, 12, 13, 14, 34, 43, 52, 61, 70, 79, // 16
			6, 7, 8, 15, 16, 24, 25, 26, 9, 10, 11, 12, 13, 14, 35, 44, 53, 62, 71, 80, // 17
			0, 1, 2, 9, 10, 11, 19, 20, 21, 22, 23, 24, 25, 26, 27, 36, 45, 54, 63, 72, // 18
			0, 1, 2, 9, 10, 11, 18, 20, 21, 22, 23, 24, 25, 26, 28, 37, 46, 55, 64, 73, // 19
			0, 1, 2, 9, 10, 11, 18, 19, 21, 22, 23, 24, 25, 26, 29, 38, 47, 56, 65, 74, // 20
			3, 4, 5, 12, 13, 14, 22, 23, 18, 19, 20, 24, 25, 26, 30, 39, 48, 57, 66, 75, // 21
			3, 4, 5, 12, 13, 14, 21, 23, 18, 19, 20, 24, 25, 26, 31, 40, 49, 58, 67, 76, // 22
			3, 4, 5, 12, 13, 14, 21, 22, 18, 19, 20, 24, 25, 26, 32, 41, 50, 59, 68, 77, // 23
			6, 7, 8, 15, 16, 17, 25, 26, 18, 19, 20, 21, 22, 23, 33, 42, 51, 60, 69, 78, // 24
			6, 7, 8, 15, 16, 17, 24, 26, 18, 19, 20, 21, 22, 23, 34, 43, 52, 61, 70, 79, // 25
			6, 7, 8, 15, 16, 17, 24, 25, 18, 19, 20, 21, 22, 23, 35, 44, 53, 62, 71, 80, // 26
			28, 29, 36, 37, 38, 45, 46, 47, 30, 31, 32, 33, 34, 35, 0, 9, 18, 54, 63, 72, // 27
			27, 29, 36, 37, 38, 45, 46, 47, 30, 31, 32, 33, 34, 35, 1, 10, 19, 55, 64, 73, // 28
			27, 28, 36, 37, 38, 45, 46, 47, 30, 31, 32, 33, 34, 35, 2, 11, 20, 56, 65, 74, // 29
			31, 32, 39, 40, 41, 48, 49, 50, 27, 28, 29, 33, 34, 35, 3, 12, 21, 57, 66, 75, // 30
			30, 32, 39, 40, 41, 48, 49, 50, 27, 28, 29, 33, 34, 35, 4, 13, 22, 58, 67, 76, // 31
			30, 31, 39, 40, 41, 48, 49, 50, 27, 28, 29, 33, 34, 35, 5, 14, 23, 59, 68, 77, // 32
			34, 35, 42, 43, 44, 51, 52, 53, 27, 28, 29, 30, 31, 32, 6, 15, 24, 60, 69, 78, // 33
			33, 35, 42, 43, 44, 51, 52, 53, 27, 28, 29, 30, 31, 32, 7, 16, 25, 61, 70, 79, // 34
			33, 34, 42, 43, 44, 51, 52, 53, 27, 28, 29, 30, 31, 32, 8, 17, 26, 62, 71, 80, // 35
			27, 28, 29, 37, 38, 45, 46, 47, 39, 40, 41, 42, 43, 44, 0, 9, 18, 54, 63, 72, // 36
			27, 28, 29, 36, 38, 45, 46, 47, 39, 40, 41, 42, 43, 44, 1, 10, 19, 55, 64, 73, // 37
			27, 28, 29, 36, 37, 45, 46, 47, 39, 40, 41, 42, 43, 44, 2, 11, 20, 56, 65, 74, // 38
			30, 31, 32, 40, 41, 48, 49, 50, 36, 37, 38, 42, 43, 44, 3, 12, 21, 57, 66, 75, // 39
			30, 31, 32, 39, 41, 48, 49, 50, 36, 37, 38, 42, 43, 44, 4, 13, 22, 58, 67, 76, // 40
			30, 31, 32, 39, 40, 48, 49, 50, 36, 37, 38, 42, 43, 44, 5, 14, 23, 59, 68, 77, // 41
			33, 34, 35, 43, 44, 51, 52, 53, 36, 37, 38, 39, 40, 41, 6, 15, 24, 60, 69, 78, // 42
			33, 34, 35, 42, 44, 51, 52, 53, 36, 37, 38, 39, 40, 41, 7, 16, 25, 61, 70, 79, // 43
			33, 34, 35, 42, 43, 51, 52, 53, 36, 37, 38, 39, 40, 41, 8, 17, 26, 62, 71, 80, // 44
			27, 28, 29, 36, 37, 38, 46, 47, 48, 49, 50, 51, 52, 53, 0, 9, 18, 54, 63, 72, // 45
			27, 28, 29, 36, 37, 38, 45, 47, 48, 49, 50, 51, 52, 53, 1, 10, 19, 55, 64, 73, // 46
			27, 28, 29, 36, 37, 38, 45, 46, 48, 49, 50, 51, 52, 53, 2, 11, 20, 56, 65, 74, // 47
			30, 31, 32, 39, 40, 41, 49, 50, 45, 46, 47, 51, 52, 53, 3, 12, 21, 57, 66, 75, // 48
			30, 31, 32, 39, 40, 41, 48, 50, 45, 46, 47, 51, 52, 53, 4, 13, 22, 58, 67, 76, // 49
			30, 31, 32, 39, 40, 41, 48, 49, 45, 46, 47, 51, 52, 53, 5, 14, 23, 59, 68, 77, // 50
			33, 34, 35, 42, 43, 44, 52, 53, 45, 46, 47, 48, 49, 50, 6, 15, 24, 60, 69, 78, // 51
			33, 34, 35, 42, 43, 44, 51, 53, 45, 46, 47, 48, 49, 50, 7, 16, 25, 61, 70, 79, // 52
			33, 34, 35, 42, 43, 44, 51, 52, 45, 46, 47, 48, 49, 50, 8, 17, 26, 62, 71, 80, // 53
			55, 56, 63, 64, 65, 72, 73, 74, 57, 58, 59, 60, 61, 62, 0, 9, 18, 27, 36, 45, // 54
			54, 56, 63, 64, 65, 72, 73, 74, 57, 58, 59, 60, 61, 62, 1, 10, 19, 28, 37, 46, // 55
			54, 55, 63, 64, 65, 72, 73, 74, 57, 58, 59, 60, 61, 62, 2, 11, 20, 29, 38, 47, // 56
			58, 59, 66, 67, 68, 75, 76, 77, 54, 55, 56, 60, 61, 62, 3, 12, 21, 30, 39, 48, // 57
			57, 59, 66, 67, 68, 75, 76, 77, 54, 55, 56, 60, 61, 62, 4, 13, 22, 31, 40, 49, // 58
			57, 58, 66, 67, 68, 75, 76, 77, 54, 55, 56, 60, 61, 62, 5, 14, 23, 32, 41, 50, // 59
			61, 62, 69, 70, 71, 78, 79, 80, 54, 55, 56, 57, 58, 59, 6, 15, 24, 33, 42, 51, // 60
			60, 62, 69, 70, 71, 78, 79, 80, 54, 55, 56, 57, 58, 59, 7, 16, 25, 34, 43, 52, // 61
			60, 61, 69, 70, 71, 78, 79, 80, 54, 55, 56, 57, 58, 59, 8, 17, 26, 35, 44, 53, // 62
			54, 55, 56, 64, 65, 72, 73, 74, 66, 67, 68, 69, 70, 71, 0, 9, 18, 27, 36, 45, // 63
			54, 55, 56, 63, 65, 72, 73, 74, 66, 67, 68, 69, 70, 71, 1, 10, 19, 28, 37, 46, // 64
			54, 55, 56, 63, 64, 72, 73, 74, 66, 67, 68, 69, 70, 71, 2, 11, 20, 29, 38, 47, // 65
			57, 58, 59, 67, 68, 75, 76, 77, 63, 64, 65, 69, 70, 71, 3, 12, 21, 30, 39, 48, // 66
			57, 58, 59, 66, 68, 75, 76, 77, 63, 64, 65, 69, 70, 71, 4, 13, 22, 31, 40, 49, // 67
			57, 58, 59, 66, 67, 75, 76, 77, 63, 64, 65, 69, 70, 71, 5, 14, 23, 32, 41, 50, // 68
			60, 61, 62, 70, 71, 78, 79, 80, 63, 64, 65, 66, 67, 68, 6, 15, 24, 33, 42, 51, // 69
			60, 61, 62, 69, 71, 78, 79, 80, 63, 64, 65, 66, 67, 68, 7, 16, 25, 34, 43, 52, // 70
			60, 61, 62, 69, 70, 78, 79, 80, 63, 64, 65, 66, 67, 68, 8, 17, 26, 35, 44, 53, // 71
			54, 55, 56, 63, 64, 65, 73, 74, 75, 76, 77, 78, 79, 80, 0, 9, 18, 27, 36, 45, // 72
			54, 55, 56, 63, 64, 65, 72, 74, 75, 76, 77, 78, 79, 80, 1, 10, 19, 28, 37, 46, // 73
			54, 55, 56, 63, 64, 65, 72, 73, 75, 76, 77, 78, 79, 80, 2, 11, 20, 29, 38, 47, // 74
			57, 58, 59, 66, 67, 68, 76, 77, 72, 73, 74, 78, 79, 80, 3, 12, 21, 30, 39, 48, // 75
			57, 58, 59, 66, 67, 68, 75, 77, 72, 73, 74, 78, 79, 80, 4, 13, 22, 31, 40, 49, // 76
			57, 58, 59, 66, 67, 68, 75, 76, 72, 73, 74, 78, 79, 80, 5, 14, 23, 32, 41, 50, // 77
			60, 61, 62, 69, 70, 71, 79, 80, 72, 73, 74, 75, 76, 77, 6, 15, 24, 33, 42, 51, // 78
			60, 61, 62, 69, 70, 71, 78, 80, 72, 73, 74, 75, 76, 77, 7, 16, 25, 34, 43, 52, // 79
			60, 61, 62, 69, 70, 71, 78, 79, 72, 73, 74, 75, 76, 77, 8, 17, 26, 35, 44, 53,// 80
	};

	private final int[] values;

	private Grid() {
		this.values = new int[81];
	}

	private Grid(final Grid other) {
		this.values = Arrays.copyOf(other.values, 81);
	}

	/**
	 * Returns the value of the specified cell.
	 * 
	 * @param column the column of the cell (one-based)
	 * @param row    the row of the cell (one-based)
	 * @return the current value of the cell, will be {@link #EMPTY_VALUE} if the
	 *         cell is empty
	 * @see #set(int, int, int)
	 * @see #empty(int, int)
	 */
	public int get(final int column, final int row) {
		return abs(this.values[index(column, row)]);
	}

	/**
	 * Sets a new value to the specified cell. The new value will replace the old
	 * value. If the cell is locked the value cannot be changed. Setting a cell to
	 * zero will empty the cell.
	 * 
	 * @param column   the column of the cell (one-based)
	 * @param row      the row of the cell (one-based)
	 * @param newValue the new value of the cell or zero
	 * @throws RuleViolationException if the new value cannot be set by Sudoku rules
	 *                                (e.g. already exsiting or locked)
	 * @see #get(int, int)
	 */
	public void set(final int column, final int row, final int newValue) throws RuleViolationException {
		final var index = index(column, row);
		final var currentValue = this.values[index];
		if (currentValue < 0)
			throw new RuleViolationException("Cannot change a locked cell");
		if (currentValue != newValue) {
			validateRules(index, validValue(newValue));
			this.values[index] = newValue;
		}
	}

	/**
	 * Checks if the specified cell is empty.
	 * 
	 * @param column the column index of the cell (one-based)
	 * @param row    the row index of the cell (one-based)
	 * @return {@code true} if the cells is empty, otherwise {@code false}
	 * @see #get(int, int)
	 */
	public boolean empty(final int column, final int row) {
		return this.values[index(column, row)] == 0;
	}

	/**
	 * Clears the specified cell by setting its value to zero.
	 * 
	 * @param column the column index of the cell (one-based)
	 * @param row    the row index of the cell (one-based)
	 * @throws RuleViolationException if the new value cannot be set by Sudoku rules
	 *                                (e.g. locked)
	 * @see #empty(int, int)
	 */
	public void clear(final int column, final int row) throws RuleViolationException {
		set(column, row, 0);
	}

	/**
	 * Locks the specified cell and sets a new value. Locked cells cannot be empty.
	 * 
	 * @param column   the column index of the cell (one-based)
	 * @param row      the row index of the cell (one-based)
	 * @param newValue the new value of the cell, but not zero
	 * @throws RuleViolationException if the value cannot be set by Sudoku rules
	 *                                (e.g. already existing or empty)
	 * @see #unlock(int, int)
	 */
	public void lock(final int column, final int row, final int newValue) throws RuleViolationException {
		if (validValue(newValue) == 0)
			throw new IllegalArgumentException("Cannot lock empty cell");
		final var index = index(column, row);
		final var currentValue = this.values[index];
		if (abs(currentValue) != newValue)
			validateRules(index, newValue);
		this.values[index] = -newValue;
	}

	/**
	 * Checks if the specified cell is locked. Locked cells cannot be changed and
	 * are never empty.
	 * 
	 * @param column the column index of the cell (one-based)
	 * @param row    the row index of the cell (one-based)
	 * @return {@code true} if the cells is locked, otherwise {@code false}
	 * @see #lock(int, int, int)
	 */
	public boolean locked(final int column, final int row) {
		return values[index(column, row)] < 0;
	}

	/**
	 * Unlocks the specified cell. The current cell value remains unchanged.
	 * 
	 * @param column the column index of the cell (one-based)
	 * @param row    the row index of the cell (one-based)
	 * @see #lock(int, int, int)
	 */
	public void unlock(final int column, final int row) {
		final var index = index(column, row);
		final var currentValue = this.values[index];
		if (currentValue < 0)
			this.values[index] = abs(currentValue);
	}

	public boolean solved() {
		for (var index = 0; index < 81; index++) {
			if (this.values[index] == 0)
				return false;
		}
		return true;
	}

	/**
	 * Returns all values that will be candidates for this cell by the Sudoku rules.
	 * The candidates depend on the values of other cells in the column, row and
	 * box. If the cell already has a value or there are no candidates for this
	 * cell, {@code null} is returned. The returned array contains only unique
	 * accepted values without {@link #EMPTY_VALUE}.
	 * 
	 * @param column the column index of the cell (one-based)
	 * @param row    the row index of the cell (one-based)
	 * @return an array with candidates or {@code null}
	 */
	public int[] candidates(final int column, final int row) {
		final var index = index(column, row);
		if (this.values[index] != 0)
			return null;

		final var candidates = new int[9];
		var candidatesdIndex = 0;

		final var dependenciesIndex = index(column, row) * 20;

		for (var value = 1; value <= 9; value++) {

			var exists = false;

			for (var i = dependenciesIndex; i < (dependenciesIndex + 20); i++) {
				if (abs(this.values[dependencies[i]]) == value) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				candidates[candidatesdIndex] = value;
				candidatesdIndex++;
			}
		}

		if (candidatesdIndex > 0)
			return Arrays.copyOf(candidates, candidatesdIndex);

		return null;
	}

	private static int validValue(final int value) {
		if (value < 0 || value > 9)
			throw new IllegalArgumentException("Invalid value: " + value);
		return value;
	}

	private void validateRules(final int index, final int newValue) throws RuleViolationException {
		if (newValue != 0) {
			final var dependenciesIndex = index * 20;

			for (var i = dependenciesIndex; i < (dependenciesIndex + 20); i++) {
				if (abs(this.values[dependencies[i]]) == newValue)
					throw new RuleViolationException(String.format("value %d already exists", newValue));
			}
		}
	}

	/**
	 * Converts the one-based column and row coordinate to an zero-based index.
	 * 
	 * @param column a one-based column
	 * @param row    a one-based row
	 * @return a zero-based one-dimensional index
	 */
	private static int index(final int column, final int row) {
		if (column < 1 || column > 9)
			throw new IllegalArgumentException("Invalid column: " + column);
		if (row < 1 || row > 9)
			throw new IllegalArgumentException("Invalid row: " + row);
		return ((row - 1) * 9) + (column - 1);
	}

	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		final var other = (Grid) obj;
		return Arrays.equals(this.values, other.values);
	}

	@Override
	public String toString() {
		final var line = "-".repeat(31) + '\n';
		final var sb = new StringBuilder(line);

		for (var row = 1; row <= 9; row++) {
			sb.append('|');
			for (var column = 1; column <= 9; column++) {
				final var value = this.values[index(column, row)];
				sb.append(value < 0 ? '<' : ' ');
				sb.append(value != 0 ? (char) (abs(value) + 0x30) : '.');
				sb.append(value < 0 ? '>' : ' ');
				if (column % 3 == 0)
					sb.append('|');
			}
			sb.append('\n');
			if (row % 3 == 0)
				sb.append(line);
		}

		return sb.toString();
	}
}