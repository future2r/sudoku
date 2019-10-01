package name.ulbricht.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SudokuSolver {

	public static SudokuSolver of(final SudokuGrid grid) {
		return new SudokuSolver(grid);
	}

	private SudokuGrid grid;

	private SudokuSolver(final SudokuGrid grid) {
		this.grid = Objects.requireNonNull(grid, "grid must not be null");
	}

	public List<SudokuGrid> solve() {
		try {
			return solve(this.grid);
		} catch (RuleViolationException ex) {
			throw new IllegalStateException("There shouldn't be any rule violations for candidate values", ex);
		}
	}

	private static List<SudokuGrid> solve(final SudokuGrid grid) throws RuleViolationException {

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
			final var solutions = new ArrayList<SudokuGrid>();
			for (var candidate : fewestCandidates) {
				final var copy = SudokuGrid.copyOf(grid);
				copy.set(fewestCanddiatesColumn, fewestCanddiatesRow, candidate);
				solutions.addAll(solve(copy));
			}
			return solutions;
		}

		return List.of();
	}
}