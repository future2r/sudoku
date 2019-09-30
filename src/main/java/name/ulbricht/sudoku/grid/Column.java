package name.ulbricht.sudoku.grid;

import java.util.Objects;

/**
 * Represents a column of values in a grid. The number of values in the column
 * depends on the number of rows in the grid. Values can be accessed by row
 * index. This index is one-based.
 */
public final class Column {

	static Column of(final Grid grid, final int columnIndex) {
		return new Column(grid, columnIndex);
	}

	private final Grid grid;
	private final int columnIndex;

	private Column(final Grid grid, final int columnIndex) {
		this.grid = Objects.requireNonNull(grid);
		this.columnIndex = columnIndex;
	}

	/**
	 * The one-based column index in the grid
	 * 
	 * @return thr column index
	 */
	public int columnIndex(){
		return this.columnIndex;
	}

	/**
	 * Returns the number of rows in the grid. This equals the number of values in
	 * this column.
	 * 
	 * @return the number of rows in the grid
	 */
	public int rows(){
		return this.grid.rows();
	}

	/**
	 * Returns the value in column at the specified row index.
	 * 
	 * @param column the one-based row index
	 * @return the value in the specified row
	 */
	public int get(int rowIndex){
		return this.grid.get(this.columnIndex, rowIndex);
	}

	/**
	 * Sets a new value in the column at the specified row index.
	 * 
	 * @param rowIndex the one-based row index
	 * @param newValue the new value in the specified row
	 */
	public void set(int rowIndex, int newValue){
		this.grid.set(this.columnIndex, rowIndex, newValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.columnIndex, this.grid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		final var other = (Column) obj;
		return this.columnIndex == other.columnIndex && Objects.equals(this.grid, other.grid);
	}

	@Override
	public String toString() {
		final var sb = new StringBuilder(String.format("columnIndex=%d, column=[", this.columnIndex));
		for (var rowIndex = 1; rowIndex <= this.rows(); rowIndex++) {
			if (rowIndex > 1)
				sb.append(", ");
			sb.append(get(rowIndex));
		}
		sb.append(']');
		return sb.toString();
	}
}