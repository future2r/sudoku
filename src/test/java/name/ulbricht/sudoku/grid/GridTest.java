package name.ulbricht.sudoku.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public final class GridTest {

	@Test
	public void testCreate() {
		final var grid = Grid.of(9, 5);
		assertEquals(9, grid.columns());
		assertEquals(5, grid.rows());

		var expectedMessage = "columns must be > 0";
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> Grid.of(0, 5)).getMessage());
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> Grid.of(-1, 5)).getMessage());

		expectedMessage = "rows must be > 0";
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> Grid.of(9, 0)).getMessage());
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> Grid.of(9, -1)).getMessage());
	}

	@Test
	public void testColumn() {
		final var grid = fillGrid(Grid.of(9, 5));

		for (var columnIndex = 1; columnIndex <= 9; columnIndex++) {
			final var column = grid.column(columnIndex);

			assertEquals(5, column.rows());
		}
	}

	@Test
	public void testRow() {
		final var grid = fillGrid(Grid.of(9, 5));

		for (var rowIndex = 1; rowIndex <= 5; rowIndex++) {
			final var row = grid.row(rowIndex);

			assertEquals(9, row.columns());
		}
	}

	@Test
	public void testSection() {
		final var grid = Grid.of(9, 5);

		final var section = grid.section(4, 2, 3, 2);

		assertEquals(4, section.startColumnIndex());
		assertEquals(2, section.startRowIndex());
		assertEquals(3, section.columns());
		assertEquals(2, section.rows());

		section.set(2, 1, 42);
		assertEquals(42, section.get(2, 1));
		assertEquals(42, grid.get(5, 2));
	}

	private static Grid fillGrid(final Grid grid) {
		for (var columnIndex = 1; columnIndex <= grid.columns(); columnIndex++) {
			for (var rowIndex = 1; rowIndex <= grid.rows(); rowIndex++) {
				grid.set(columnIndex, rowIndex, columnIndex * 10 + rowIndex);
			}
		}
		return grid;
	}
}