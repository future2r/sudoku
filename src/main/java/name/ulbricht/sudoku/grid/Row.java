package name.ulbricht.sudoku.grid;

/**
 * Represents a row of values in a grid. The number of values in the row depends
 * on the number of columns in the grid. Values can be accessed by column index.
 * This index is one-based.
 */
public interface Row {

	/**
	 * Returns the one-based row index in the grid
	 * 
	 * @return thr row index
	 */
	int rowIndex();

	/**
	 * Returns the number of columns in the grid. This equals the number of values
	 * in this row.
	 * 
	 * @return the number of columns in the grid
	 */
	int columns();

	/**
	 * Returns the value in the row at the specified column index.
	 * 
	 * @param columnIndex the one-based column index
	 * @return the value in the specified column
	 */
	int get(int columnIndex);

	/**
	 * Sets a new value in the row at the specified column index.
	 * 
	 * @param columnIndex the one-based column index
	 * @param newValue the new value in the specified column
	 */
	void set(int columnIndex, int newValue);
}