package name.ulbricht.sudoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

final class Sudokus {

	static final String INITIAL_PATTERN = "5.1.3..6.\n" //
			+ "...4..2..\n" //
			+ "7........\n" //
			+ ".28......\n" //
			+ "....7...1\n" //
			+ ".......3.\n" //
			+ "16.2.....\n" //
			+ "........4\n" //
			+ "......8..";

	static final int[][] INITIAL_CELLS = { { 1, 1, 5 }, { 3, 1, 1 }, { 5, 1, 3 }, { 8, 1, 6 }, { 4, 2, 4 }, { 7, 2, 2 },
			{ 1, 3, 7 }, { 2, 4, 2 }, { 3, 4, 8 }, { 5, 5, 7 }, { 9, 5, 1 }, { 8, 6, 3 }, { 1, 7, 1 }, { 2, 7, 6 },
			{ 4, 7, 2 }, { 9, 8, 4 }, { 7, 9, 8 } };

	static final String SOLVED_PATTERN = "591832467\n" //
			+ "836417295\n" //
			+ "742569183\n" //
			+ "928351746\n" //
			+ "453678921\n" //
			+ "617924538\n" //
			+ "164285379\n" //
			+ "285793614\n" //
			+ "379146852";

	static SudokuGrid load(final String baseName) throws IOException {
		final var fileName = Paths.get(System.getProperty("user.dir"), "files", baseName + ".sudoku");
		return SudokuGrid.of(Files.readString(fileName));
	}
}