package name.ulbricht.sudoku.grid;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a grid of values. A grid is described by the number columns and
 * rows. Values can be accessed by column and row coordinates. These coordinates
 * are one-based.
 */
public final class Grid {

	/**
	 * Creates a new grid with the given column and row count.
	 * 
	 * @param columns the number of columns
	 * @param rows    the number of rows
	 * @return a new grid with columns by rows values
	 */
	public static Grid of(final int columns, final int rows) {
		return new Grid(columns, rows);
	}

	/**
	 * Creates an independent copy the specified grid.
	 * 
	 * @param original the original to copy from
	 * @return an independent copy
	 */
	public static Grid copyOf(final Grid original) {
		return new Grid(original);
	}

	private final int columns;
	private final int rows;
	private final int[] values;

	private Grid(final int columns, final int rows) {
		if (columns <= 0)
			throw new IllegalArgumentException("columns must be > 0");
		if (rows <= 0)
			throw new IllegalArgumentException("rows must be > 0");

		this.columns = columns;
		this.rows = rows;
		this.values = new int[this.columns * this.rows];
	}

	private Grid(final Grid other) {
		this.columns = other.columns;
		this.rows = other.rows;
		this.values = Arrays.copyOf(other.values, other.values.length);
	}

	/**
	 * Returns the number of columns in this grid.
	 * 
	 * @return the number of columns
	 */
	public int columns() {
		return this.columns;
	}

	/**
	 * Returns the number of rows in this grid.
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
		return this.values[index(columnIndex, rowIndex)];
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
		this.values[index(columnIndex, rowIndex)] = newValue;
	}

	/**
	 * Returns an object for accessing the value of a single column.
	 * 
	 * @param columnIndex the one-based column index
	 * @return a column object
	 */
	public Column column(int columnIndex) {
		return Column.of(this, validColumn(columnIndex));
	}

	/**
	 * Returns an object for accessing the value of a single row.
	 * 
	 * @param rowIndex the one-based row index
	 * @return a row object
	 */
	public Row row(int rowIndex) {
		return Row.of(this, validRow(rowIndex));
	}

	/**
	 * Returns an object rpresenting a section of the grid. The section
	 * 
	 * @param startColumn the one-based column index where the section starts
	 * @param startRow    the one-based row index where the section starts
	 * @param columns     the number of columns in the section
	 * @param rows        the number of rows in the section
	 * @return a grid representing the section
	 */
	public Section section(int startColumn, int startRow, int columns, int rows) {
		return Section.of(this, startColumn, startRow, columns, rows);
	}

	private int index(final int columnIndex, final int rowIndex) {
		return ((validColumn(columnIndex) - 1) * this.rows()) + (validRow(rowIndex) - 1);
	}

	private int validColumn(final int columnIndex) throws IndexOutOfBoundsException {
		if (columnIndex < 1 || columnIndex > this.columns)
			throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
		return columnIndex;
	}

	private int validRow(final int rowIndex) throws IndexOutOfBoundsException {
		if (rowIndex < 1 || rowIndex > this.rows)
			throw new IndexOutOfBoundsException("Invalid row index: " + rowIndex);
		return rowIndex;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.columns, this.rows, this.values.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		final var other = (Grid) obj;
		return this.columns == other.columns && this.rows == other.rows && Arrays.equals(this.values, other.values);
	}

	@Override
	public String toString() {
		final var sb = new StringBuilder(String.format("columns=%d, rows=%d, values=\n", this.columns, this.rows));
		for (var rowIndex = 1; rowIndex <= this.rows; rowIndex++) {
			if (rowIndex > 1)
				sb.append('\n');
			for (var columnIndex = 1; columnIndex <= this.columns; columnIndex++) {
				if (columnIndex > 1)
					sb.append(", ");
				sb.append(get(columnIndex, rowIndex));
			}
		}
		return sb.toString();
	}
}