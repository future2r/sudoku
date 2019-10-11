package name.ulbricht.sudoku;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
		for (int columnIndex = 1; columnIndex <= 9; columnIndex++) {
			for (int rowIndex = 1; rowIndex <= 9; rowIndex++) {
				builder.add(Arguments.of(columnIndex, rowIndex));
			}
		}
		return builder.build();
	}

	public static Stream<Arguments> allColumnsAndRowsAndValues() {
		final var builder = Stream.<Arguments>builder();
		for (var columnIndex = 1; columnIndex <= 9; columnIndex++) {
			for (var rowIndex = 1; rowIndex <= 9; rowIndex++) {
				for (var value = 1; value <= 9; value++) {
					builder.add(Arguments.of(columnIndex, rowIndex, value));
				}
			}
		}
		return builder.build();
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testCreateEmpty(final int columnIndex, final int rowIndex) {
		final var grid = Grid.empty();
		assertTrue(grid.empty(columnIndex, rowIndex));
		assertEquals(0, grid.get(columnIndex, rowIndex));
		assertFalse(grid.locked(columnIndex, rowIndex));
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testOf(final int columnIndex, final int rowIndex) {
		final var grid = Grid.of(Grids.INITIAL_PATTERN);

		var tested = false;

		for (var cell : Grids.INITIAL_CELLS) {
			if (cell[0] == columnIndex && cell[1] == rowIndex) {
				assertFalse(grid.empty(columnIndex, rowIndex));
				assertEquals(cell[2], grid.get(columnIndex, rowIndex));
				assertFalse(grid.locked(columnIndex, rowIndex));
				tested = true;
				break;
			}
		}

		if (!tested) {
			assertTrue(grid.empty(columnIndex, rowIndex));
			assertEquals(0, grid.get(columnIndex, rowIndex));
			assertFalse(grid.locked(columnIndex, rowIndex));
		}
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testOfLocked(final int columnIndex, final int rowIndex) {
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);

		var tested = false;

		for (var cell : Grids.INITIAL_CELLS) {
			if (cell[0] == columnIndex && cell[1] == rowIndex) {
				assertFalse(grid.empty(columnIndex, rowIndex));
				assertEquals(cell[2], grid.get(columnIndex, rowIndex));
				assertTrue(grid.locked(columnIndex, rowIndex));
				tested = true;
				break;
			}
		}

		if (!tested) {
			assertTrue(grid.empty(columnIndex, rowIndex));
			assertEquals(0, grid.get(columnIndex, rowIndex));
			assertFalse(grid.locked(columnIndex, rowIndex));
		}
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRowsAndValues")
	public void testSetGetClearValues(final int columnIndex, final int rowIndex, final int value)
			throws RuleViolationException {
		final var grid = Grid.empty();

		grid.set(columnIndex, rowIndex, value);
		assertEquals(value, grid.get(columnIndex, rowIndex));

		grid.clear(columnIndex, rowIndex);
		assertTrue(grid.empty(columnIndex, rowIndex));
		assertEquals(0, grid.get(columnIndex, rowIndex));
	}

	@ParameterizedTest
	@ValueSource(ints = { -100, -10, -1, 0, 10, 100 })
	public void testInvalidColumn(final int columnIndex) {
		final var grid = Grid.empty();
		final var expectedMessage = "Invalid column: " + columnIndex;

		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.get(columnIndex, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.set(columnIndex, 1, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.clear(columnIndex, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.empty(columnIndex, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.lock(columnIndex, 1, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.unlock(columnIndex, 1)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.locked(columnIndex, 1)).getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = { -100, -10, -1, 0, 10, 100 })
	public void testInvalidRow(final int rowIndex) {
		final var grid = Grid.empty();
		final var expectedMessage = "Invalid row: " + rowIndex;

		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.get(1, rowIndex)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.set(1, rowIndex, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.clear(1, rowIndex)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.empty(1, rowIndex)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.lock(1, rowIndex, 3)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.unlock(1, rowIndex)).getMessage());
		assertEquals(expectedMessage,
				assertThrows(IllegalArgumentException.class, () -> grid.locked(1, rowIndex)).getMessage());
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
	public void testLockUnlock(final int columnIndex, final int rowIndex, final int value)
			throws RuleViolationException {
		final var grid = Grid.empty();

		grid.lock(columnIndex, rowIndex, value);
		assertTrue(grid.locked(columnIndex, rowIndex));
		assertEquals(value, grid.get(columnIndex, rowIndex));

		grid.unlock(columnIndex, rowIndex);
		assertFalse(grid.locked(columnIndex, rowIndex));
		assertEquals(value, grid.get(columnIndex, rowIndex));
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testLockEmpty(final int columnIndex, final int rowIndex) {
		final var grid = Grid.empty();

		assertEquals("Cannot lock empty cell",
				assertThrows(IllegalArgumentException.class, () -> grid.lock(columnIndex, rowIndex, 0)).getMessage());
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRowsAndValues")
	public void testRules(final int columnIndex, final int rowIndex, final int value) throws RuleViolationException {
		// set a cell
		final var grid = Grid.empty();
		grid.set(8, 4, 3);

		var tested = false;

		// same value in same box
		if (value == 3 && (columnIndex >= 7 && columnIndex <= 9) && (rowIndex >= 4 && rowIndex <= 6)
				&& !(columnIndex == 8 && rowIndex == 4)) {
			assertEquals("value 3 already exists",
					assertThrows(RuleViolationException.class, () -> grid.set(columnIndex, rowIndex, value))
							.getMessage());
			tested = true;
		}

		// same value in same column
		if (!tested && value == 3 && columnIndex == 8 && rowIndex != 4) {
			assertEquals("value 3 already exists",
					assertThrows(RuleViolationException.class, () -> grid.set(columnIndex, rowIndex, value))
							.getMessage());
			tested = true;
		}

		// same value in same row
		if (!tested && value == 3 && rowIndex == 4 && columnIndex != 8) {
			assertEquals("value 3 already exists",
					assertThrows(RuleViolationException.class, () -> grid.set(columnIndex, rowIndex, value))
							.getMessage());
			tested = true;
		}

		// the remaining cells and values should work
		if (!tested)
			grid.set(columnIndex, rowIndex, value);
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testCandidatesInitial(final int columnIndex, final int rowIndex) {
		// create initial grid
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);

		if (grid.empty(columnIndex, rowIndex))
			assertTrue(grid.candidates(columnIndex, rowIndex).length > 0);
		else
			assertNull(grid.candidates(columnIndex, rowIndex));
	}

	@ParameterizedTest
	@MethodSource("allColumnsAndRows")
	public void testCandidatesSolved(final int columnIndex, final int rowIndex) {
		// create initial grid
		final var grid = Grid.ofLocked(Grids.SOLVED_PATTERN);

		// all cells are filled, nothing is accepted
		assertNull(grid.candidates(columnIndex, rowIndex));
	}

	@Test
	public void testAccepted() {
		// create initial grid
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);

		// box 1
		assertCandidates(grid, 2, 1, 4, 8, 9);
		assertCandidates(grid, 1, 2, 3, 6, 8, 9);
		assertCandidates(grid, 2, 2, 3, 8, 9);
		assertCandidates(grid, 3, 2, 3, 6, 9);
		assertCandidates(grid, 2, 3, 3, 4, 8, 9);
		assertCandidates(grid, 3, 3, 2, 3, 4, 6, 9);

		// box 2
		assertCandidates(grid, 4, 1, 7, 8, 9);
		assertCandidates(grid, 6, 1, 2, 7, 8, 9);
		assertCandidates(grid, 5, 2, 1, 5, 6, 8, 9);
		assertCandidates(grid, 6, 2, 1, 5, 6, 7, 8, 9);
		assertCandidates(grid, 4, 3, 1, 5, 6, 8, 9);
		assertCandidates(grid, 5, 3, 1, 2, 5, 6, 8, 9);
		assertCandidates(grid, 6, 3, 1, 2, 5, 6, 8, 9);

		// box 3
		assertCandidates(grid, 7, 1, 4, 7, 9);
		assertCandidates(grid, 9, 1, 7, 8, 9);
		assertCandidates(grid, 8, 2, 1, 5, 7, 8, 9);
		assertCandidates(grid, 9, 2, 3, 5, 7, 8, 9);
		assertCandidates(grid, 7, 3, 1, 3, 4, 5, 9);
		assertCandidates(grid, 8, 3, 1, 4, 5, 8, 9);
		assertCandidates(grid, 9, 3, 3, 5, 8, 9);

		// box 4
		assertCandidates(grid, 1, 4, 3, 4, 6, 9);
		assertCandidates(grid, 1, 5, 3, 4, 6, 9);
		assertCandidates(grid, 2, 5, 3, 4, 5, 9);
		assertCandidates(grid, 3, 5, 3, 4, 5, 6, 9);
		assertCandidates(grid, 1, 6, 4, 6, 9);
		assertCandidates(grid, 2, 6, 1, 4, 5, 7, 9);
		assertCandidates(grid, 3, 6, 4, 5, 6, 7, 9);

		// box 5
		assertCandidates(grid, 4, 4, 1, 3, 5, 6, 9);
		assertCandidates(grid, 5, 4, 1, 4, 5, 6, 9);
		assertCandidates(grid, 6, 4, 1, 3, 4, 5, 6, 9);
		assertCandidates(grid, 4, 5, 3, 5, 6, 8, 9);
		assertCandidates(grid, 6, 5, 2, 3, 4, 5, 6, 8, 9);
		assertCandidates(grid, 4, 6, 1, 5, 6, 8, 9);
		assertCandidates(grid, 5, 6, 1, 2, 4, 5, 6, 8, 9);
		assertCandidates(grid, 6, 6, 1, 2, 4, 5, 6, 8, 9);

		// box 6
		assertCandidates(grid, 7, 4, 4, 5, 6, 7, 9);
		assertCandidates(grid, 8, 4, 4, 5, 7, 9);
		assertCandidates(grid, 9, 4, 5, 6, 7, 9);
		assertCandidates(grid, 7, 5, 4, 5, 6, 9);
		assertCandidates(grid, 8, 5, 2, 4, 5, 8, 9);
		assertCandidates(grid, 7, 6, 4, 5, 6, 7, 9);
		assertCandidates(grid, 9, 6, 2, 5, 6, 7, 8, 9);

		// box 7
		assertCandidates(grid, 3, 7, 3, 4, 5, 7, 9);
		assertCandidates(grid, 1, 8, 2, 3, 8, 9);
		assertCandidates(grid, 2, 8, 3, 5, 7, 8, 9);
		assertCandidates(grid, 3, 8, 2, 3, 5, 7, 9);
		assertCandidates(grid, 1, 9, 2, 3, 4, 9);
		assertCandidates(grid, 2, 9, 3, 4, 5, 7, 9);
		assertCandidates(grid, 3, 9, 2, 3, 4, 5, 7, 9);

		// box 8
		assertCandidates(grid, 5, 7, 4, 5, 8, 9);
		assertCandidates(grid, 6, 7, 3, 4, 5, 7, 8, 9);
		assertCandidates(grid, 4, 8, 1, 3, 5, 6, 7, 8, 9);
		assertCandidates(grid, 5, 8, 1, 5, 6, 8, 9);
		assertCandidates(grid, 6, 8, 1, 3, 5, 6, 7, 8, 9);
		assertCandidates(grid, 4, 9, 1, 3, 5, 6, 7, 9);
		assertCandidates(grid, 5, 9, 1, 4, 5, 6, 9);
		assertCandidates(grid, 6, 9, 1, 3, 4, 5, 6, 7, 9);

		// box 9
		assertCandidates(grid, 7, 7, 3, 5, 7, 9);
		assertCandidates(grid, 8, 7, 5, 7, 9);
		assertCandidates(grid, 9, 7, 3, 5, 7, 9);
		assertCandidates(grid, 7, 8, 1, 3, 5, 6, 7, 9);
		assertCandidates(grid, 8, 8, 1, 2, 5, 7, 9);
		assertCandidates(grid, 8, 9, 1, 2, 5, 7, 9);
		assertCandidates(grid, 9, 9, 2, 3, 5, 6, 7, 9);
	}

	private void assertCandidates(final Grid grid, final int columnIndex, final int rowIndex,
			final int... expected) {
		final var candidates = grid.candidates(columnIndex, rowIndex);
		assertArrayEquals(expected, candidates, String.format("cell %d,%d: expected: %s but was %s", columnIndex,
				rowIndex, Arrays.toString(expected), Arrays.toString(candidates)));
	}

	@Test
	public void testHashCodeAndEquals() {
		final var grid1 = Grid.ofLocked(Grids.INITIAL_PATTERN);
		final var grid2 = Grid.ofLocked(Grids.SOLVED_PATTERN);

		assertEquals(grid1, grid1);
		assertEquals(grid2, grid2);

		assertNotEquals(grid1, grid2);
		assertNotEquals(grid1.hashCode(), grid2.hashCode());
		assertFalse(grid1.equals(grid2));
		assertFalse(grid2.equals(grid1));
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

	@Test
	public void testCopy() {
		final var grid = Grid.ofLocked(Grids.INITIAL_PATTERN);
		final var copy = Grid.copyOf(grid);

		assertEquals(copy, grid);
	}
}