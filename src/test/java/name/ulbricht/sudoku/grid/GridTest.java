package name.ulbricht.sudoku.grid;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public final class GridTest {

	@Test
	public void testCreate() {
		final var grid = Grid.of(String.class, 9, 5);
		assertEquals(9, grid.columns());
		assertEquals(5, grid.rows());

		var expectedMessage = "columns must be > 0";
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> IntGrid.of(0, 5)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> IntGrid.of(-1, 5)).getMessage());

		expectedMessage = "rows must be > 0";
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> IntGrid.of(9, 0)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> IntGrid.of(9, -1)).getMessage());
	}

	@Test
	public void testColumn() {
		final var grid = fillGrid(Grid.of(String.class, 9, 5));

		for (var columnIndex = 1; columnIndex <= 9; columnIndex++) {
			final var column = grid.column(columnIndex);

			assertEquals(5, column.rows());
		}
	}

	@Test
	public void testRow() {
		final var grid = fillGrid(Grid.of(String.class, 9, 5));

		for (var rowIndex = 1; rowIndex <= 5; rowIndex++) {
			final var row = grid.row(rowIndex);

			assertEquals(9, row.columns());
		}
	}

	@Test
	public void testSection() {
		final var grid = Grid.of(String.class, 9, 5);

		final var section = grid.section(4, 2, 3, 2);

		assertEquals(4, section.startColumnIndex());
		assertEquals(2, section.startRowIndex());
		assertEquals(3, section.columns());
		assertEquals(2, section.rows());

		section.set(2, 1, "Hello");
		assertEquals("Hello", section.get(2, 1));
		assertEquals("Hello", grid.get(5, 2));
	}

	@Test
	public void testHashCodeAndEquals() {
		final var grid1 = Grid.of(String.class, 9, 5);
		final var grid2 = fillGrid(Grid.of(String.class, 9, 5));

		assertEquals(grid1, grid1);
		assertEquals(grid2, grid2);

		assertNotEquals(grid1, grid2);
		assertNotEquals(grid1.hashCode(), grid2.hashCode());
		assertFalse(grid1.equals(grid2));
		assertFalse(grid2.equals(grid1));
	}

	@Test
	public void testToString() {
		final var grid = fillGrid(Grid.of(String.class, 9, 5));

		assertEquals("columns=9, rows=5, values=\n" //
				+ "1-1, 2-1, 3-1, 4-1, 5-1, 6-1, 7-1, 8-1, 9-1\n" //
				+ "1-2, 2-2, 3-2, 4-2, 5-2, 6-2, 7-2, 8-2, 9-2\n" //
				+ "1-3, 2-3, 3-3, 4-3, 5-3, 6-3, 7-3, 8-3, 9-3\n" //
				+ "1-4, 2-4, 3-4, 4-4, 5-4, 6-4, 7-4, 8-4, 9-4\n" //
				+ "1-5, 2-5, 3-5, 4-5, 5-5, 6-5, 7-5, 8-5, 9-5", //
				grid.toString());

		assertEquals("columnIndex=2, column=[2-1, 2-2, 2-3, 2-4, 2-5]", grid.column(2).toString());
		assertEquals("rowIndex=3, row=[1-3, 2-3, 3-3, 4-3, 5-3, 6-3, 7-3, 8-3, 9-3]", grid.row(3).toString());
	}

	private static Grid<String> fillGrid(final Grid<String> grid) {
		for (var columnIndex = 1; columnIndex <= grid.columns(); columnIndex++) {
			for (var rowIndex = 1; rowIndex <= grid.rows(); rowIndex++) {
				grid.set(columnIndex, rowIndex, columnIndex + "-" + rowIndex);
			}
		}
		return grid;
	}

}