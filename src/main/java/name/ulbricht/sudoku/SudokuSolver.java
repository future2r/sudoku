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
		var fewestCanddiatesColumnIndex = 0;
		var fewestCanddiatesRowIndex = 0;

		boolean changed;
		do {
			if (grid.solved())
				return List.of(grid);

			changed = false;
			for (var rowIndex = 1; rowIndex <= SudokuGrid.GRID_SIZE; rowIndex++) {
				for (var columnIndex = 1; columnIndex <= SudokuGrid.GRID_SIZE; columnIndex++) {

					final var candidates = grid.candidates(columnIndex, rowIndex);
					if (candidates != null) {
						if (candidates.length == 1) {
							grid.set(columnIndex, rowIndex, candidates[0]);
							changed = true;
							fewestCandidates = null;
							fewestCanddiatesColumnIndex = 0;
							fewestCanddiatesRowIndex = 0;
							break;
						}
						if (fewestCandidates == null || candidates.length < fewestCandidates.length) {
							fewestCandidates = candidates;
							fewestCanddiatesColumnIndex = columnIndex;
							fewestCanddiatesRowIndex = rowIndex;
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
				copy.set(fewestCanddiatesColumnIndex, fewestCanddiatesRowIndex, candidate);
				solutions.addAll(solve(copy));
			}
			return solutions;
		}

		return List.of();
	}
}