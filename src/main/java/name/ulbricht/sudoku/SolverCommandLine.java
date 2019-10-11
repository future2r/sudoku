package name.ulbricht.sudoku;

import static name.ulbricht.sudoku.Messages.msg;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;

final class SolverCommandLine {

	private final PrintStream out;
	private final Locale locale;

	SolverCommandLine(final PrintStream out, final Locale locale) {
		this.out = out;
		this.locale = locale;
	}

	int run(final String... args) {
		Path sourceFile = null;

		for (final String arg : args) {
			if (sourceFile != null) {
				this.out.println(msg(this.locale, "SolverCommandLine.unexpectedArgument", arg));
				return 1;
			}
			try {
				sourceFile = Path.of(arg);
			} catch (final InvalidPathException ex) {
				this.out.println(msg(this.locale, "SolverCommandLine.invalidFileName", arg));
				return 1;
			}
		}

		if (sourceFile == null) {
			this.out.println(msg(this.locale, "SolverCommandLine.noSourceFileSpecified"));
			return 1;
		}

		Grid grid;
		try {
			grid = GridFile.parse(sourceFile);
		} catch (final IOException ex) {
			this.out.println(msg(this.locale, "SolverCommandLine.parseGridFileError", ex.getLocalizedMessage()));
			return 1;
		}

		final var solver = Solver.of(grid);
		solver.solve();

		this.out.println(msg(this.locale, "SolverCommandLine.numberOfSolutions", solver.solutionCount()));
		this.out.println(msg(this.locale, "SolverCommandLine.solutionTime", solver.solutionTime()));
		for (final Grid solution : solver.solutions()) {
			this.out.println(solution.toString());
		}

		return 0;
	}
}