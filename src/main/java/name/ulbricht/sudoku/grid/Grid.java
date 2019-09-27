package name.ulbricht.sudoku.grid;

/**
 * Represents a grid of values. A grid is described by the number columns and
 * rows. Values can be accessed by column and row coordinates. These coordinates
 * are one-based.
 */
public interface Grid {

	/**
	 * Creates a new grid with the given column and row count.
	 * 
	 * @param columns the number of columns
	 * @param rows    the number of rows
	 * @return a new grid with columns by rows values
	 */
	static Grid of(final int columns, final int rows) {
		return new GridImpl(columns, rows);
	}

	/**
	 * Returns the number of columns in this grid.
	 * 
	 * @return the number of columns
	 */
	int columns();

	/**
	 * Returns the number of rows in this grid.
	 * 
	 * @return the number of rows
	 */
	int rows();

	/**
	 * Returns a value at the specified column and row coordinates. The column and
	 * row are one-based.
	 * 
	 * @param columnIndex the one-based column index
	 * @param rowIndex    the one-based row index
	 * @return the value in the specified coordinates
	 */
	int get(final int columnIndex, final int rowIndex);

	/**
	 * Sets a new value at the specified column and row coordinates. The column and
	 * row are one-based.
	 * 
	 * @param columnIndex the one-based column index
	 * @param rowIndex    the one-based row index
	 * @param newValue  the new value
	 */
	void set(final int columnIndex, final int rowIndex, final int newValue);

	/**
	 * Returns an object for accessing the value of a single column.
	 * 
	 * @param columnIndex the one-based column index
	 * @return a column object
	 */
	Column column(int columnIndex);

	/**
	 * Returns an object for accessing the value of a single row.
	 * 
	 * @param rowIndex the one-based row index
	 * @return a row object
	 */
	Row row(int rowIndex);

	/**
	 * Returns an object rpresenting a section of the grid. The section
	 * 
	 * @param startColumn the one-based column index where the section starts
	 * @param startRow    the one-based row index where the section starts
	 * @param columns     the number of columns in the section
	 * @param rows        the number of rows in the section
	 * @return a grid representing the section
	 */
	Section section(int startColumn, int startRow, int columns, int rows);
}