package name.ulbricht.sudoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

final class Grids {

	static final String INITIAL_PATTERN = """
			5.1.3..6.
			...4..2..
			7........
			.28......
			....7...1
			.......3.
			16.2.....
			........4
			......8..
			""";

	static final int[][] INITIAL_CELLS = { { 1, 1, 5 }, { 3, 1, 1 }, { 5, 1, 3 }, { 8, 1, 6 }, { 4, 2, 4 }, { 7, 2, 2 },
			{ 1, 3, 7 }, { 2, 4, 2 }, { 3, 4, 8 }, { 5, 5, 7 }, { 9, 5, 1 }, { 8, 6, 3 }, { 1, 7, 1 }, { 2, 7, 6 },
			{ 4, 7, 2 }, { 9, 8, 4 }, { 7, 9, 8 } };

	static final String SOLVED_PATTERN = """
			591832467
			836417295
			742569183
			928351746
			453678921
			617924538
			164285379
			285793614
			379146852
			""";

	static Grid load(final String fileName) throws IOException {
		final var path = Paths.get(System.getProperty("user.dir"), "files", fileName);
		return Grid.of(Files.readString(path));
	}
}