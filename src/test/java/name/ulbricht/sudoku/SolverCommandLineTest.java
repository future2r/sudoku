package name.ulbricht.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class SolverCommandLineTest {

	private static final Locale locale = Locale.ENGLISH;

	private ByteArrayOutputStream outContent;
	private PrintStream out;
	private SolverCommandLine commandLine;

	@BeforeEach
	public void beforeEach() {
		this.outContent = new ByteArrayOutputStream();
		this.out = new PrintStream(this.outContent);
		commandLine = new SolverCommandLine(this.out, locale);
	}

	@AfterEach
	public void afterEach() throws IOException {
		this.out.close();
		this.outContent.close();
	}

	@Test
	public void testSolveFile() {
		assertEquals(0, commandLine.run("files/single 1.sudoku"));
		assertOutStartsWith(String.format("Number of solutions: 1%nSolution time: "));
	}

	@Test
	public void testNoFileSpecified() {
		assertEquals(1, commandLine.run());
		assertOutEquals(String.format("No Sudoku file specified.%n"));
	}

	@Test
	public void testUnexpectedArgument() {
		assertEquals(1, commandLine.run("grid.sudoku", "fast"));
		assertOutEquals(String.format("Unexpected argument: fast%n"));
	}

	@Test
	public void testMissingFile() {
		assertEquals(1, commandLine.run("missing.sudoku"));
		assertOutStartsWith("Could not read the Sudoku file: ");
	}

	@Test
	public void testInvalidFileName() {
		assertEquals(1, commandLine.run("*?:"));
		assertOutStartsWith("Invalid file name: ");
	}

	@Test
	public void testInvalidFile() {
		assertEquals(1, commandLine.run("files/invalid.sudoku"));
		assertOutStartsWith("Could not read the Sudoku file: ");
	}

	private void assertOutEquals(final String expected) {
		assertEquals(expected, this.outContent.toString());
	}

	private void assertOutStartsWith(final String expected) {
		final var actual = this.outContent.toString();
		assertTrue(actual.startsWith(expected),
				String.format("Expected to start with \"%s\" but was \"%s\"", expected, actual));
	}
}