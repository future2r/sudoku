package name.ulbricht.sudoku.grid;

import java.util.Arrays;

final class GridImpl extends AbstractGrid {

	static GridImpl copyOf(final GridImpl original) {
		return new GridImpl(original);
	}

	private final int[] values;

	GridImpl(final int columns, final int rows) {
		super(columns, rows);

		this.values = new int[columns() * rows()];
	}

	GridImpl(final GridImpl other) {
		super(other.columns(), other.rows());
		this.values = Arrays.copyOf(other.values, other.values.length);
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

	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		final var other = (GridImpl) obj;
		return Arrays.equals(this.values, other.values);
	}

	@Override
	public String toString() {
		final var sb = new StringBuilder();
		for (var rowIndex = 1; rowIndex <= this.rows(); rowIndex++) {
			if (rowIndex > 1)
					sb.append('\n');
			for (var columnIndex = 1; columnIndex <= this.columns(); columnIndex++) {
				if (columnIndex > 1)
					sb.append(", ");
				sb.append(get(columnIndex, rowIndex));
			}
		}
		return sb.toString();
	}
}