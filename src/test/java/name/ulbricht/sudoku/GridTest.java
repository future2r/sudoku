package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
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

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testAcceptedInitial(final int column, final int row) {
		// create initial grid
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);

		if (grid.empty(column, row))
			assertTrue(grid.accepted(column, row).length > 0);
		else
			assertEquals(0, grid.accepted(column, row).length);
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testAcceptedSolved(final int column, final int row) {
		// create initial grid
		final var grid = Grid.ofLocked(Grids.SOLVED_PATTERN);

		// all cells are filled, nothing is accepted
		assertEquals(0, grid.accepted(column, row).length);
	}

	@Test
	public void testAccepted() {
		// create initial grid
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);

		// box 1
		assetAccepted(grid, 2, 1, 4, 8, 9);
		assetAccepted(grid, 1, 2, 3, 6, 8, 9);
		assetAccepted(grid, 2, 2, 3, 8, 9);
		assetAccepted(grid, 3, 2, 3, 6, 9);
		assetAccepted(grid, 2, 3, 3, 4, 8, 9);
		assetAccepted(grid, 3, 3, 2, 3, 4, 6, 9);

		// box 2
		assetAccepted(grid, 4, 1, 7, 8, 9);
		assetAccepted(grid, 6, 1, 2, 7, 8, 9);
		assetAccepted(grid, 5, 2, 1, 5, 6, 8, 9);
		assetAccepted(grid, 6, 2, 1, 5, 6, 7, 8, 9);
		assetAccepted(grid, 4, 3, 1, 5, 6, 8, 9);
		assetAccepted(grid, 5, 3, 1, 2, 5, 6, 8, 9);
		assetAccepted(grid, 6, 3, 1, 2, 5, 6, 8, 9);

		// box 3
		assetAccepted(grid, 7, 1, 4, 7, 9);
		assetAccepted(grid, 9, 1, 7, 8, 9);
		assetAccepted(grid, 8, 2, 1, 5, 7, 8, 9);
		assetAccepted(grid, 9, 2, 3, 5, 7, 8, 9);
		assetAccepted(grid, 7, 3, 1, 3, 4, 5, 9);
		assetAccepted(grid, 8, 3, 1, 4, 5, 8, 9);
		assetAccepted(grid, 9, 3, 3, 5, 8, 9);

		// box 4
		assetAccepted(grid, 1, 4, 3, 4, 6, 9);
		assetAccepted(grid, 1, 5, 3, 4, 6, 9);
		assetAccepted(grid, 2, 5, 3, 4, 5, 9);
		assetAccepted(grid, 3, 5, 3, 4, 5, 6, 9);
		assetAccepted(grid, 1, 6, 4, 6, 9);
		assetAccepted(grid, 2, 6, 1, 4, 5, 7, 9);
		assetAccepted(grid, 3, 6, 4, 5, 6, 7, 9);

		// box 5
		assetAccepted(grid, 4, 4, 1, 3, 5, 6, 9);
		assetAccepted(grid, 5, 4, 1, 4, 5, 6, 9);
		assetAccepted(grid, 6, 4, 1, 3, 4, 5, 6, 9);
		assetAccepted(grid, 4, 5, 3, 5, 6, 8, 9);
		assetAccepted(grid, 6, 5, 2, 3, 4, 5, 6, 8, 9);
		assetAccepted(grid, 4, 6, 1, 5, 6, 8, 9);
		assetAccepted(grid, 5, 6, 1, 2, 4, 5, 6, 8, 9);
		assetAccepted(grid, 6, 6, 1, 2, 4, 5, 6, 8, 9);

		// box 6
		assetAccepted(grid, 7, 4, 4, 5, 6, 7, 9);
		assetAccepted(grid, 8, 4, 4, 5, 7, 9);
		assetAccepted(grid, 9, 4, 5, 6, 7, 9);
		assetAccepted(grid, 7, 5, 4, 5, 6, 9);
		assetAccepted(grid, 8, 5, 2, 4, 5, 8, 9);
		assetAccepted(grid, 7, 6, 4, 5, 6, 7, 9);
		assetAccepted(grid, 9, 6, 2, 5, 6, 7, 8, 9);

		// box 7
		assetAccepted(grid, 3, 7, 3, 4, 5, 7, 9);
		assetAccepted(grid, 1, 8, 2, 3, 8, 9);
		assetAccepted(grid, 2, 8, 3, 5, 7, 8, 9);
		assetAccepted(grid, 3, 8, 2, 3, 5, 7, 9);
		assetAccepted(grid, 1, 9, 2, 3, 4, 9);
		assetAccepted(grid, 2, 9, 3, 4, 5, 7, 9);
		assetAccepted(grid, 3, 9, 2, 3, 4, 5, 7, 9);

		// box 8
		assetAccepted(grid, 5, 7, 4, 5, 8, 9);
		assetAccepted(grid, 6, 7, 3, 4, 5, 7, 8, 9);
		assetAccepted(grid, 4, 8, 1, 3, 5, 6, 7, 8, 9);
		assetAccepted(grid, 5, 8, 1, 5, 6, 8, 9);
		assetAccepted(grid, 6, 8, 1, 3, 5, 6, 7, 8, 9);
		assetAccepted(grid, 4, 9, 1, 3, 5, 6, 7, 9);
		assetAccepted(grid, 5, 9, 1, 4, 5, 6, 9);
		assetAccepted(grid, 6, 9, 1, 3, 4, 5, 6, 7, 9);

		// box 9
		assetAccepted(grid, 7, 7, 3, 5, 7, 9);
		assetAccepted(grid, 8, 7, 5, 7, 9);
		assetAccepted(grid, 9, 7, 3, 5, 7, 9);
		assetAccepted(grid, 7, 8, 1, 3, 5, 6, 7, 9);
		assetAccepted(grid, 8, 8, 1, 2, 5, 7, 9);
		assetAccepted(grid, 8, 9, 1, 2, 5, 7, 9);
		assetAccepted(grid, 9, 9, 2, 3, 5, 6, 7, 9);
	}

	private void assetAccepted(final Grid grid, final int column, final int row, final int... expected) {
		final var accepted = grid.accepted(column, row);
		assertArrayEquals(expected, accepted, String.format("cell %d,%d: expected: %s but was %s", column, row,
				Arrays.toString(expected), Arrays.toString(accepted)));
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