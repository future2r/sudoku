package name.ulbricht.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

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

		solve(this.grid);

		this.solutionTime = System.currentTimeMillis() - startTime;
	}

	private void addSolution(final Grid solution) {
		synchronized (this.solutions) {
			this.solutions.size();
			this.solutions.add(solution);
		}
	}

	private void solve(final Grid grid) {
		if (this.solutions.size() >= 10)
			return;

		int[] fewestCandidates = null;
		var fewestCanddiatesColumn = 0;
		var fewestCanddiatesRow = 0;

		boolean changed;
		do {
			if (grid.solved())
				addSolution(grid);

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
			final var column = fewestCanddiatesColumn;
			final var row = fewestCanddiatesRow;
			IntStream.of(fewestCandidates).parallel().mapToObj(candidate -> setCandidate(grid, column, row, candidate))
					.forEach(this::solve);
		}
	}

	private static Grid setCandidate(final Grid original, final int column, final int row, final int value) {
		final var copy = Grid.copyOf(original);
		copy.set(column, row, value);
		return copy;
	}
}