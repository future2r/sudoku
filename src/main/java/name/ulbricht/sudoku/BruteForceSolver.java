package name.ulbricht.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class BruteForceSolver implements Solver {

	private final Grid grid;
	private final List<Grid> solutions = new ArrayList<>();
	private long solutionTime;

	BruteForceSolver(final Grid grid) {
		this.grid = Objects.requireNonNull(grid, "grid must not be null");
	}

	@Override
	public long solutionTime() {
		return this.solutionTime;
	}

	@Override
	public int solutionCount() {
		return this.solutions.size();
	}

	@Override
	public List<Grid> solutions() {
		return new ArrayList<>(this.solutions);
	}

	@Override
	public void solve() {
		this.solutions.clear();

		this.solutionTime = 0;
		final var startTime = System.currentTimeMillis();

		try {
			solve(this.grid);
		} catch (RuleViolationException ex) {
			throw new IllegalStateException("There shouldn't be any rule violations for candidate values", ex);
		}

		this.solutionTime = System.currentTimeMillis() - startTime;
	}

	private void solve(final Grid grid) throws RuleViolationException {

		int[] fewestCandidates = null;
		var fewestCanddiatesColumn = 0;
		var fewestCanddiatesRow = 0;

		boolean changed;
		do {
			if (grid.solved())
				this.solutions.add(grid);

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
			for (var candidate : fewestCandidates) {
				final var copy = Grid.copyOf(grid);
				copy.set(fewestCanddiatesColumn, fewestCanddiatesRow, candidate);
				solve(copy);
			}
		}
	}
}