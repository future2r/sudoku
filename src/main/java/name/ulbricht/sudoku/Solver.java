package name.ulbricht.sudoku;

import java.util.List;
import java.util.Locale;

public interface Solver {

	static void main(final String... args) {
		final var commandLine = new SolverCommandLine(System.out, Locale.getDefault());
		final var exitCode = commandLine.run(args);
		if (exitCode != 0)
			System.exit(exitCode);
	}

	static Solver of(final Grid grid) {
		return new BruteForceSolver(grid);
	}

	void solve();

	long solutionTime();

	int solutionCount();

	List<Grid> solutions();
}