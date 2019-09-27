package name.ulbricht.sudoku.grid;

abstract class AbstractGrid implements Grid {

	private final int columns;
	private final int rows;

	AbstractGrid(final int columns, final int rows) {
		if (columns <= 0)
			throw new IllegalArgumentException("columns must be > 0");
		if (rows <= 0)
			throw new IllegalArgumentException("rows must be > 0");

		this.columns = columns;
		this.rows = rows;
	}

	@Override
	public final int columns() {
		return this.columns;
	}

	@Override
	public final int rows() {
		return this.rows;
	}

	@Override
	public final Column column(final int columnIndex) {
		return ColumnImpl.of(this, validColumn(columnIndex));
	}

	@Override
	public final Row row(final int rowIndex) {
		return RowImpl.of(this, validRow(rowIndex));
	}

	@Override
	public final Section section(int startColumn, int startRow, int columns, int rows) {
		return new SectionImpl(this, startColumn, startRow, columns, rows);
	}

	protected final int validColumn(final int columnIndex) throws IndexOutOfBoundsException {
		if (columnIndex < 1 || columnIndex > this.columns)
			throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
		return columnIndex;
	}

	protected final int validRow(final int rowIndex) throws IndexOutOfBoundsException {
		if (rowIndex < 1 || rowIndex > this.rows)
			throw new IndexOutOfBoundsException("Invalid row index: " + rowIndex);
		return rowIndex;
	}
}