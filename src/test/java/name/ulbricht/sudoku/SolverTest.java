package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public final class SolverTest {
	
	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	public void testUniqueSolution(final int number) throws IOException {
		final var initialGrid = Grids.load("initial" + number);
		final var solvedGrid = Grids.load("solved" + number);

		final var solver = Solver.of(initialGrid);
		final var solutions = solver.solve();

		assertEquals(1, solutions.size());
		assertEquals(solvedGrid, solutions.get(0));
	}
}