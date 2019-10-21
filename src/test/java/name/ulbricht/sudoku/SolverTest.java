package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public final class SolverTest {

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	public void testUniqueSolution(final int number) throws IOException {
		final var initialGrid = Grids.load(String.format("single %s.sudoku", number));
		final var solvedGrid = Grids.load(String.format("single %s (solved).sudoku", number));

		final var solver = Solver.of(initialGrid);
		solver.solve();

		assertEquals(1, solver.solutionCount());

		final var solutions = solver.solutions();
		assertEquals(1, solutions.size());
		assertEquals(solvedGrid, solutions.get(0));
	}

	@Test
	public void testMultipleSolutions() throws IOException {
		final var initialGrid = Grids.load("multiple 1.sudoku");
		final var solvedGrid1 = Grids.load("multiple 1 (solved 1).sudoku");
		final var solvedGrid2 = Grids.load("multiple 1 (solved 2).sudoku");

		final var solver = Solver.of(initialGrid);
		solver.solve();

		assertEquals(2, solver.solutionCount());
		final var solutions = solver.solutions();
		assertEquals(2, solutions.size());
		assertTrue(solutions.contains(solvedGrid1));
		assertTrue(solutions.contains(solvedGrid2));
	}

	@Test
	public void testManySolutions() throws IOException {
		final var initialGrid = Grids.load("multiple 2.sudoku");

		final var solver = Solver.of(initialGrid);
		solver.solve();

		assertTrue(solver.solutionCount() >= 10);
		final var solutions = solver.solutions();
		assertTrue(solutions.size() >= 10);
	}
}