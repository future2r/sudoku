package name.ulbricht.sudoku.grid;

import java.util.Objects;

/**
 * Represents a section of a grid which is a grid itself.
 */
public final class IntSection {

	static IntSection of(final IntGrid grid, final int startColumnIndex, final int startRowIndex, final int columns,
			final int rows) {
		return new IntSection(grid, startColumnIndex, startRowIndex, columns, rows);
	}

	private final IntGrid grid;
	private final int startColumnIndex;
	private final int startRowIndex;
	private final int columns;
	private final int rows;

	private IntSection(final IntGrid grid, final int startColumnIndex, final int startRowIndex, final int columns,
			final int rows) {

		this.grid = Objects.requireNonNull(grid);

		if (startColumnIndex <= 0)
			throw new IllegalArgumentException("startColumn must be > 0 but was " + startColumnIndex);
		if (startRowIndex <= 0)
			throw new IllegalArgumentException("startRow must be > 0 but was " + startRowIndex);
		this.startColumnIndex = startColumnIndex;
		this.startRowIndex = startRowIndex;

		if (columns <= 0)
			throw new IllegalArgumentException("columns must be > 0");
		if (rows <= 0)
			throw new IllegalArgumentException("rows must be > 0");
		this.columns = columns;
		this.rows = rows;

		if (((startColumnIndex + columns() - 1) > grid.columns()) || ((startRowIndex + rows() - 1) > grid.rows()))
			throw new IllegalArgumentException("section exceeds the grid");

	}

	/**
	 * Returns the start column index of this section within the grid.
	 * 
	 * @return the start column index within the grid
	 */
	public int startColumnIndex() {
		return this.startColumnIndex;
	}

	/**
	 * Returns the start row index of this section within the grid.
	 * 
	 * @return the start row index within the grid
	 */
	public int startRowIndex() {
		return this.startRowIndex;
	}

	/**
	 * Returns the number of columns in this section.
	 * 
	 * @return the number of columns
	 */
	public int columns() {
		return this.columns;
	}

	/**
	 * Returns the number of rows in this section.
	 * 
	 * @return the number of rows
	 */
	public int rows() {
		return this.rows;
	}

	/**
	 * Returns a value at the specified column and row coordinates. The column and
	 * row are one-based.
	 * 
	 * @param columnIndex the one-based column index
	 * @param rowIndex    the one-based row index
	 * @return the value in the specified coordinates
	 */
	public int get(final int columnIndex, final int rowIndex) {
		return this.grid.get(startColumnIndex + columnIndex - 1, startRowIndex + rowIndex - 1);
	}

	/**
	 * Sets a new value at the specified column and row coordinates. The column and
	 * row are one-based.
	 * 
	 * @param columnIndex the one-based column index
	 * @param rowIndex    the one-based row index
	 * @param newValue    the new value
	 */
	public void set(final int columnIndex, final int rowIndex, final int newValue) {
		this.grid.set(startColumnIndex + columnIndex - 1, startRowIndex + rowIndex - 1, newValue);
	}
}