package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public final class SudokuSolverTest {

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	public void testUniqueSolution(final int number) throws IOException {
		final var initialGrid = Sudokus.load("initial" + number);
		final var solvedGrid = Sudokus.load("solved" + number);

		final var solver = SudokuSolver.of(initialGrid);
		final var solutions = solver.solve();

		assertEquals(1, solutions.size());
		assertEquals(solvedGrid, solutions.get(0));
	}
}