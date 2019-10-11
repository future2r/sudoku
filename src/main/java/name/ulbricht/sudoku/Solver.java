package name.ulbricht.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Solver {

	public static Solver of(final Grid grid) {
		return new Solver(grid);
	}

	private Grid grid;

	private Solver(final Grid grid) {
		this.grid = Objects.requireNonNull(grid, "grid must not be null");
	}

	public List<Grid> solve() {
		try {
			return solve(this.grid);
		} catch (RuleViolationException ex) {
			throw new IllegalStateException("There shouldn't be any rule violations for candidate values", ex);
		}
	}

	private static List<Grid> solve(final Grid grid) throws RuleViolationException {

		int[] fewestCandidates = null;
		var fewestCanddiatesColumn = 0;
		var fewestCanddiatesRow = 0;

		boolean changed;
		do {
			if (grid.solved())
				return List.of(grid);

			changed = false;

			for (var column = 1; column <= 9; column++) {
				for (var row = 1; row <= 9; row++) {
					final var candidates = grid.candidates(column, row);
					if (candidates != null) {
						if (candidates.length == 1) {
							grid.set(column, row, candidates[0]);
							changed = true;
							fewestCandidates = null;
							fewestCanddiatesColumn = 0;
							fewestCanddiatesRow = 0;
							break;
						}
						if (fewestCandidates == null || candidates.length < fewestCandidates.length) {
							fewestCandidates = candidates;
							fewestCanddiatesColumn = column;
							fewestCanddiatesRow = row;
						}
					}
				}
				if (changed)
					break;
			}
		} while (changed);

		if (fewestCandidates != null) {
			final var solutions = new ArrayList<Grid>();
			for (var candidate : fewestCandidates) {
				final var copy = Grid.copyOf(grid);
				copy.set(fewestCanddiatesColumn, fewestCanddiatesRow, candidate);
				solutions.addAll(solve(copy));
			}
			return solutions;
		}

		return List.of();
	}
}