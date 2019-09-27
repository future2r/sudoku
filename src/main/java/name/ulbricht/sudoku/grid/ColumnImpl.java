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
}