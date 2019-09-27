package name.ulbricht.sudoku.grid;

import java.util.Objects;

final class ColumnImpl implements Column {

	static Column of(final Grid grid, final int columnIndex) {
		return new ColumnImpl(grid, columnIndex);
	}

	private final Grid grid;
	private final int columnIndex;

	ColumnImpl(final Grid grid, final int columnIndex) {
		this.grid = Objects.requireNonNull(grid);
		this.columnIndex = columnIndex;
	}

	@Override
	public int columnIndex() {
		return this.columnIndex;
	}

	@Override
	public int rows() {
		return this.grid.rows();
	}

	@Override
	public int get(final int rowIndex) {
		return this.grid.get(this.columnIndex, rowIndex);
	}

	@Override
	public void set(final int rowIndex, final int newValue) {
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
		final var other = (ColumnImpl) obj;
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