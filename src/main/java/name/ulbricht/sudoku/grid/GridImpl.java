package name.ulbricht.sudoku.grid;

final class GridImpl extends AbstractGrid {

	private final int[] values;

	GridImpl(final int columns, final int rows) {
		super(columns, rows);

		this.values = new int[columns() * rows()];
	}

	@Override
	public int get(final int columnIndex, final int rowIndex) {
		return this.values[index(columnIndex, rowIndex)];
	}

	@Override
	public void set(final int columnIndex, final int rowIndex, final int newValue) {
		this.values[index(columnIndex, rowIndex)] = newValue;
	}

	private int index(final int columnIndex, final int rowIndex) {
		return ((validColumn(columnIndex) - 1) * this.rows()) + (validRow(rowIndex) - 1);
	}
}