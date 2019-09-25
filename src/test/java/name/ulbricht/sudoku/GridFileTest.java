package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public final class GridFileTest {

	@ParameterizedTest
	@ValueSource(strings = { "empty", "initial", "solved" })
	public void parseWriteFiles(final String fileName) throws IOException {
		// parse the file
		final var grid = GridFile.parse(resolveFileName(fileName));

		// write the file to string
		final String[] fileContent;
		try (final var writer = new StringWriter()) {
			GridFile.write(writer, grid);
			writer.flush();
			fileContent = writer.toString().split("\\r\\n?|\\n");
		}

		// compare the strings
		String[] expected = loadFileContent(fileName);
		assertArrayEquals(expected, fileContent);
	}

	private static String[] loadFileContent(final String fileName) throws IOException {
		return Files.lines(resolveFileName(fileName)).toArray(String[]::new);
	}

	private static Path resolveFileName(final String fileName) {
		return Paths.get(System.getProperty("user.dir"), "files", fileName + ".sudoku");
	}
}