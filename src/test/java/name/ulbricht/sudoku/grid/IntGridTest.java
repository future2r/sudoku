package name.ulbricht.sudoku.grid;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public final class IntGridTest {

	@Test
	public void testCreate() {
		final var grid = IntGrid.of(9, 5);
		assertEquals(9, grid.columns());
		assertEquals(5, grid.rows());

		var expectedMessage = "columns must be > 0";
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> IntGrid.of(0, 5)).getMessage());
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> IntGrid.of(-1, 5)).getMessage());

		expectedMessage = "rows must be > 0";
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> IntGrid.of(9, 0)).getMessage());
		assertEquals(expectedMessage, assertThrows(IllegalArgumentException.class, () -> IntGrid.of(9, -1)).getMessage());
	}

	@Test
	public void testColumn() {
		final var grid = fillGrid(IntGrid.of(9, 5));

		for (var columnIndex = 1; columnIndex <= 9; columnIndex++) {
			final var column = grid.column(columnIndex);

			assertEquals(5, column.rows());
		}
	}

	@Test
	public void testRow() {
		final var grid = fillGrid(IntGrid.of(9, 5));

		for (var rowIndex = 1; rowIndex <= 5; rowIndex++) {
			final var row = grid.row(rowIndex);

			assertEquals(9, row.columns());
		}
	}

	@Test
	public void testSection() {
		final var grid = IntGrid.of(9, 5);

		final var section = grid.section(4, 2, 3, 2);

		assertEquals(4, section.startColumnIndex());
		assertEquals(2, section.startRowIndex());
		assertEquals(3, section.columns());
		assertEquals(2, section.rows());

		section.set(2, 1, 42);
		assertEquals(42, section.get(2, 1));
		assertEquals(42, grid.get(5, 2));
	}

	@Test
	public void testHashCodeAndEquals() {
		final var grid1 = IntGrid.of(9, 5);
		final var grid2 = fillGrid(IntGrid.of(9, 5));

		assertEquals(grid1, grid1);
		assertEquals(grid2, grid2);

		assertNotEquals(grid1, grid2);
		assertNotEquals(grid1.hashCode(), grid2.hashCode());
		assertFalse(grid1.equals(grid2));
		assertFalse(grid2.equals(grid1));
	}

	@Test
	public void testToString() {
		final var grid = fillGrid(IntGrid.of(9, 5));

		assertEquals("columns=9, rows=5, values=\n" //
				+ "11, 21, 31, 41, 51, 61, 71, 81, 91\n" //
				+ "12, 22, 32, 42, 52, 62, 72, 82, 92\n" //
				+ "13, 23, 33, 43, 53, 63, 73, 83, 93\n" //
				+ "14, 24, 34, 44, 54, 64, 74, 84, 94\n" //
				+ "15, 25, 35, 45, 55, 65, 75, 85, 95", //
				grid.toString());

		assertEquals("columnIndex=2, column=[21, 22, 23, 24, 25]", grid.column(2).toString());
		assertEquals("rowIndex=3, row=[13, 23, 33, 43, 53, 63, 73, 83, 93]", grid.row(3).toString());
	}

	private static IntGrid fillGrid(final IntGrid grid) {
		for (var columnIndex = 1; columnIndex <= grid.columns(); columnIndex++) {
			for (var rowIndex = 1; rowIndex <= grid.rows(); rowIndex++) {
				grid.set(columnIndex, rowIndex, columnIndex * 10 + rowIndex);
			}
		}
		return grid;
	}

}