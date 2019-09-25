package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public final class GridTest {

	public static Stream<Arguments> allColumnsAndRows() {
		final var builder = Stream.<Arguments>builder();
		for (int column = 1; column <= Grid.GRID_SIZE; column++) {
			for (int row = 1; row <= Grid.GRID_SIZE; row++) {
				builder.add(Arguments.of(column, row));
			}
		}
		return builder.build();
	}

	public static Stream<Arguments> allColumnsAndRowsAndValues() {
		final var builder = Stream.<Arguments>builder();
		for (var column = 1; column <= Grid.GRID_SIZE; column++) {
			for (var row = 1; row <= Grid.GRID_SIZE; row++) {
				for (var value = Grid.MIN_VALUE; value <= Grid.MAX_VALUE; value++) {
					builder.add(Arguments.of(column, row, value));
				}
			}
		}
		return builder.build();
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testCreateEmpty(final int column, final int row) {
		final var grid = Grid.empty();
		assertTrue(grid.empty(column, row));
		assertEquals(Grid.EMPTY_VALUE, grid.get(column, row));
		assertFalse(grid.locked(column, row));
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testOf(final int column, final int row) {
		final var grid = Grid.of(Grids.INITIAL_PATTERN);

		var tested = false;

		for (var cell : Grids.INITIAL_CELLS) {
			if (cell[0] == column && cell[1] == row) {
				assertFalse(grid.empty(column, row));
				assertEquals(cell[2], grid.get(column, row));
				assertFalse(grid.locked(column, row));
				tested = true;
				break;
			}
		}

		if (!tested) {
			assertTrue(grid.empty(column, row));
			assertEquals(Grid.EMPTY_VALUE, grid.get(column, row));
			assertFalse(grid.locked(column, row));
		}
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testOfLocked(final int column, final int row) {
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);

		var tested = false;

		for (var cell : Grids.INITIAL_CELLS) {
			if (cell[0] == column && cell[1] == row) {
				assertFalse(grid.empty(column, row));
				assertEquals(cell[2], grid.get(column, row));
				assertTrue(grid.locked(column, row));
				tested = true;
				break;
			}
		}

		if (!tested) {
			assertTrue(grid.empty(column, row));
			assertEquals(Grid.EMPTY_VALUE, grid.get(column, row));
			assertFalse(grid.locked(column, row));
		}
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRowsAndValues")
	public void testSetGetClearValues(final int column, final int row, final int value) throws RuleViolationException {
		final var grid = Grid.empty();

		grid.set(column, row, value);
		assertEquals(value, grid.get(column, row));

		grid.clear(column, row);
		assertTrue(grid.empty(column, row));
		assertEquals(Grid.EMPTY_VALUE, grid.get(column, row));
	}

	@ParameterizedTest
	@ValueSource(ints = { -100, -10, -1, 0, 10, 100 })
	public void testInvalidColumn(final int column) {
		final var grid = Grid.empty();
		final var expectedMessage = "Invalid column: " + column;

		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.get(column, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.set(column, 1, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.clear(column, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.empty(column, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.lock(column, 1, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.unlock(column, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.locked(column, 1)).getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = { -100, -10, -1, 0, 10, 100 })
	public void testInvalidRow(final int row) {
		final var grid = Grid.empty();
		final var expectedMessage = "Invalid row: " + row;

		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.get(1, row)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.set(1, row, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.clear(1, row)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.empty(1, row)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.lock(1, row, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.unlock(1, row)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IndexOutOfBoundsException.class, () -> grid.locked(1, row)).getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 10 })
	public void testInvalidValue(final int value) {
		final var grid = Grid.empty();
		final var expectedMessage = "Invalid value: " + value;

		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.set(1, 1, value)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.lock(1, 1, value)).getMessage());
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRowsAndValues")
	public void testLockUnlock(final int column, final int row, final int value) throws RuleViolationException {
		final var grid = Grid.empty();

		grid.lock(column, row, value);
		assertTrue(grid.locked(column, row));
		assertEquals(value, grid.get(column, row));

		grid.unlock(column, row);
		assertFalse(grid.locked(column, row));
		assertEquals(value, grid.get(column, row));
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testLockEmpty(final int column, final int row) {
		final var grid = Grid.empty();

		assertEquals("Cannot lock empty cell",
				assertThrows(IllegalArgumentException.class, () -> grid.lock(column, row, Grid.EMPTY_VALUE))
						.getMessage());
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRowsAndValues")
	public void testRules(final int column, final int row, final int value) throws RuleViolationException {
		// set a cell
		final var grid = Grid.empty();
		grid.set(8, 4, 3);

		var tested = false;

		// same value in same box
		if (value == 3 && (column >= 7 && column <= 9) && (row >= 4 && row <= 6) && !(column == 8 && row == 4)) {
			assertEquals("value 3 already exists in box",
					assertThrows(RuleViolationException.class, () -> grid.set(column, row, value)).getMessage());
			tested = true;
		}

		// same value in same column
		if (!tested && value == 3 && column == 8 && row != 4) {
			assertEquals("value 3 already exists in column " + column,
					assertThrows(RuleViolationException.class, () -> grid.set(column, row, value)).getMessage());
			tested = true;
		}

		// same value in same row
		if (!tested && value == 3 && row == 4 && column != 8) {
			assertEquals("value 3 already exists in row " + row,
					assertThrows(RuleViolationException.class, () -> grid.set(column, row, value)).getMessage());
			tested = true;
		}

		// the remaining cells and values should work
		if (!tested)
			grid.set(column, row, value);
	}

	@Test
	public void testToString() throws RuleViolationException {
		// create initial grid
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);

		// solve upper left box
		grid.set(2, 1, 9);
		grid.set(1, 2, 8);
		grid.set(2, 2, 3);
		grid.set(3, 2, 6);
		grid.set(2, 3, 4);
		grid.set(3, 3, 2);

		// compare expected output
		final var expected = "-------------------------------\n" //
				+ "|<5> 9 <1>| . <3> . | . <6> . |\n" //
				+ "| 8  3  6 |<4> .  . |<2> .  . |\n" //
				+ "|<7> 4  2 | .  .  . | .  .  . |\n" //
				+ "-------------------------------\n" //
				+ "| . <2><8>| .  .  . | .  .  . |\n" //
				+ "| .  .  . | . <7> . | .  . <1>|\n" //
				+ "| .  .  . | .  .  . | . <3> . |\n" //
				+ "-------------------------------\n" //
				+ "|<1><6> . |<2> .  . | .  .  . |\n" //
				+ "| .  .  . | .  .  . | .  . <4>|\n" //
				+ "| .  .  . | .  .  . |<8> .  . |\n" //
				+ "-------------------------------\n";
		assertEquals(expected, grid.toString());
	}
}