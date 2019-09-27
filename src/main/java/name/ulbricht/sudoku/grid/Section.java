package name.ulbricht.sudoku.grid;

/**
 * Represents a section of a grid which is a grid itself.
 */
public interface Section extends Grid {

	/**
	 * Returns the start column index of this section within the grid.
	 * @return the start column index within the grid
	 */
	int startColumnIndex();

	/**
	 * Returns the start row index of this section within the grid.
	 * @return the start row index within the grid
	 */
	int startRowIndex();
}