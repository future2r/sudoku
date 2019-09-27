package name.ulbricht.sudoku.grid;

/**
 * Represents a column of values in a grid. The number of values in the column depends
 * on the number of rows in the grid. Values can be accessed by row index.
 * This index is one-based.
 */
public interface Column {

	/**
	 * The one-based column index in the grid
	 * 
	 * @return thr column index
	 */
	int columnIndex();

	/**
	 * Returns the number of rows in the grid. This equals the number of values in
	 * this column.
	 * 
	 * @return the number of rows in the grid
	 */
	int rows();

	/**
	 * Returns the value in column at the specified row index.
	 * 
	 * @param column the one-based row index
	 * @return the value in the specified row
	 */
	int get(int rowIndex);

	/**
	 * Sets a new value in the column at the specified row index.
	 * 
	 * @param rowIndex the one-based row index
	 * @param newValue the new value in the specified row
	 */
	void set(int rowIndex, int newValue);
}