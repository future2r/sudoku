package name.ulbricht.sudoku.grid;

import java.util.Objects;

final class RowImpl implements Row {

	static Row of(final Grid grid, final int rowIndex) {
		return new RowImpl(grid, rowIndex);
	}

	private final Grid grid;
	private final int rowIndex;

	RowImpl(final Grid grid, final int rowIndex) {
		this.grid = Objects.requireNonNull(grid);
		this.rowIndex = rowIndex;
	}

	@Override
	public int rowIndex() {
		return this.rowIndex;
	}

	@Override
	public int columns() {
		return this.grid.columns();
	}

	@Override
	public int get(final int columnIndex) {
		return this.grid.get(columnIndex, this.rowIndex);
	}

	@Override
	public void set(int columnIndex, int newValue) {
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
		final var other = (RowImpl) obj;
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