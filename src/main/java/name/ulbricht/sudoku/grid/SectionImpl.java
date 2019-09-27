package name.ulbricht.sudoku.grid;

import java.util.Objects;

final class SectionImpl extends AbstractGrid implements Section {

	private final Grid grid;
	private final int startColumnIndex;
	private final int startRowIndex;

	SectionImpl(final Grid grid, final int startColumnIndex, final int startRowIndex, final int columns,
			final int rows) {
		super(columns, rows);

		if (startColumnIndex <= 0)
			throw new IllegalArgumentException("startColumn must be > 0 but was " + startColumnIndex);
		if (startRowIndex <= 0)
			throw new IllegalArgumentException("startRow must be > 0 but was " + startRowIndex);

		this.grid = Objects.requireNonNull(grid);

		if (((startColumnIndex + columns() - 1) > grid.columns()) || ((startRowIndex + rows() - 1) > grid.rows()))
			throw new IllegalArgumentException("section exceeds the grid");

		this.startColumnIndex = startColumnIndex;
		this.startRowIndex = startRowIndex;
	}

	@Override
	public int startColumnIndex() {
		return this.startColumnIndex;
	}

	@Override
	public int startRowIndex() {
		return this.startRowIndex;
	}

	@Override
	public int get(final int columnIndex, final int rowIndex) {
		return this.grid.get(startColumnIndex + columnIndex - 1, startRowIndex + rowIndex - 1);
	}

	@Override
	public void set(final int columnIndex, final int rowIndex, final int newValue) {
		this.grid.set(startColumnIndex + columnIndex - 1, startRowIndex + rowIndex - 1, newValue);
	}
}