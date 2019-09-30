package name.ulbricht.sudoku.grid;

import java.util.Objects;

/**
 * Represents a row of values in a grid. The number of values in the row depends
 * on the number of columns in the grid. Values can be accessed by column index.
 * This index is one-based.
 * 
 * @param <T> the type of the values
 */
public final class Row<T> {

	static <T> Row<T> of(final Grid<T> grid, final int rowIndex) {
		return new Row<>(grid, rowIndex);
	}

	private final Grid<T> grid;
	private final int rowIndex;

	private Row(final Grid<T> grid, final int rowIndex) {
		this.grid = Objects.requireNonNull(grid);
		this.rowIndex = rowIndex;
	}

	/**
	 * Returns the one-based row index in the grid
	 * 
	 * @return thr row index
	 */
	public int rowIndex() {
		return this.rowIndex;
	}

	/**
	 * Returns the number of columns in the grid. This equals the number of values
	 * in this row.
	 * 
	 * @return the number of columns in the grid
	 */
	public int columns() {
		return this.grid.columns();
	}

	/**
	 * Returns the value in the row at the specified column index.
	 * 
	 * @param columnIndex the one-based column index
	 * @return the value in the specified column
	 */
	public T get(int columnIndex) {
		return this.grid.get(columnIndex, this.rowIndex);
	}

	/**
	 * Sets a new value in the row at the specified column index.
	 * 
	 * @param columnIndex the one-based column index
	 * @param newValue    the new value in the specified column
	 */
	public void set(int columnIndex, T newValue) {
		this.grid.set(columnIndex, this.rowIndex, newValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.rowIndex, this.grid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		final var other = (Row<?>) obj;
		return this.rowIndex == other.rowIndex && Objects.equals(this.grid, other.grid);
	}

	@Override
	public String toString() {
		final var sb = new StringBuilder(String.format("rowIndex=%d, row=[", this.rowIndex));
		for (var columnIndex = 1; columnIndex <= this.columns(); columnIndex++) {
			if (columnIndex > 1)
				sb.append(", ");
			sb.append(get(columnIndex));
		}
		sb.append(']');
		return sb.toString();
	}
}