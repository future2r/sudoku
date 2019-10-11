package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

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
		assertEquals(solvedGrid, solutions.get(0));
	}
}