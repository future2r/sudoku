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
}