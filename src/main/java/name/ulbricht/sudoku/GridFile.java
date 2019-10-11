package name.ulbricht.sudoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Parses and writes Sudoku patterns. A Sudoku pattern consists of 9 lines
 * repesenting the rows of a grid. Each line consists of 9 characters
 * representing the cells in the rows. The characters '1' to '9' representing
 * the cell values, a single dot represents an empty cell. Empty lines and lines
 * starting with the comment character '#' are ignored.
 */
public final class GridFile {

	private static final char COMMENT_PREFIX = '#';
	private static final char EMPTY_CELL = '.';

	/**
	 * Parses the content of the given file and creates a new grid. All non-empty
	 * cells will be locked.
	 * 
	 * @param file the file to parse
	 * @return a new grid
	 * @throws IOException if there is a problem reading the file
	 */
	public static Grid parse(final Path file) throws IOException {
		return parse(file, true);
	}

	/**
	 * Parses the content of the given file and creates a new grid.
	 * 
	 * @param file   the file to parse
	 * @param locked defines if the non-empty cells should be locked
	 * @return a new grid
	 * @throws IOException if there is a problem reading the file
	 */
	public static Grid parse(final Path file, final boolean locked) throws IOException {
		try (final var in = Files.newInputStream(file)) {
			return parse(in, locked);
		}
	}

	/**
	 * Parses the content from the given stream source and creates a new grid. All
	 * non-empty cells will be locked.
	 * 
	 * @param in the source to read from
	 * @return a new grid
	 * @throws IOException if there is a problem reading the data
	 */
	public static Grid parse(final InputStream in) throws IOException {
		return parse(in, true);
	}

	/**
	 * Parses the content from the given stream source and creates a new grid.
	 * 
	 * @param in     the source to read from
	 * @param locked defines if the non-empty cells should be locked
	 * @return a new grid
	 * @throws IOException if there is a problem reading the data
	 */
	public static Grid parse(final InputStream in, final boolean locked) throws IOException {
		try (final var reader = new InputStreamReader(in)) {
			return parse(reader, locked);
		}
	}

	/**
	 * Parses the content from the given reader source and creates a new grid. All
	 * non-empty cells will be locked.
	 * 
	 * @param in the source to read from
	 * @return a new grid
	 * @throws IOException if there is a problem reading the data
	 */
	public static Grid parse(final Reader in) throws IOException {
		return parse(in, true);
	}

	/**
	 * Parses the content from the given reader source and creates a new grid.
	 * 
	 * @param in     the source to read from
	 * @param locked defines if the non-empty cells should be locked
	 * @return a new grid
	 * @throws IOException if there is a problem reading the data
	 */
	public static Grid parse(final Reader in, final boolean locked) throws IOException {
		try (final var br = new BufferedReader(in)) {
			final var grid = Grid.empty();

			var row = 1;
			String line = null;
			while ((line = br.readLine()) != null) {
				if (row > 9)
					throw new IOException("Too many rows");

				if (line.length() > 0 && line.charAt(0) == COMMENT_PREFIX)
					continue;

				parseLine(grid, row, line, locked);
				row++;
			}

			if (row < 9)
				throw new IOException("Too few rows");

			return grid;
		}
	}

	/**
	 * Parses the content from the given string and creates a new grid. All
	 * non-empty cells will be locked.
	 * 
	 * @param in the string to read from
	 * @return a new grid
	 * @throws IOException if there is a problem reading the string
	 */
	public static Grid parse(final String s) throws IOException {
		return parse(s, true);
	}

	/**
	 * Parses the content from the given string and creates a new grid.
	 * 
	 * @param in     the string to read from
	 * @param locked defines if the non-empty cells should be locked
	 * @return a new grid
	 * @throws IOException if there is a problem reading the string
	 */
	public static Grid parse(final String s, final boolean locked) throws IOException {
		try (final var in = new StringReader(s)) {
			return parse(in, locked);
		}
	}

	/**
	 * Parses a signle line into a grid row.
	 * 
	 * @param grid   the grid to write to
	 * @param row    the row of the grid to write to
	 * @param line   the line to parse from
	 * @param locked defines if the non-empty cells should be locked
	 * @throws IOException if there is a problem reading the line
	 */
	private static void parseLine(final Grid grid, final int row, final String line, final boolean locked)
			throws IOException {
		if (line.length() != 9)
			throw new IOException("Unexpected line length: " + line.length());

		for (var column = 1; column <= 9; column++) {
			var c = line.charAt(column - 1);

			if (c == EMPTY_CELL)
				continue;

			try {
				final var value = c - 0x30;
				if (locked)
					grid.lock(column, row, value);
				else
					grid.set(column, row, value);
			} catch (IllegalArgumentException | RuleViolationException ex) {
				throw new IOException("Invalid value", ex);
			}
		}
	}

	/**
	 * Writes the given grid into the specified file.
	 * 
	 * @param file the file to write to
	 * @param grid the grid to write
	 * @throws IOException if there is a problem while writing the grid data
	 */
	public static void write(final Path file, final Grid grid) throws IOException {
		try (final var out = Files.newOutputStream(file)) {
			write(out, grid);
		}
	}

	/**
	 * Writes the given grid to the specified stream.
	 * 
	 * @param file the stream to write to
	 * @param grid the grid to write
	 * @throws IOException if there is a problem while writing the grid data
	 */
	public static void write(final OutputStream out, final Grid grid) throws IOException {
		try (final var writer = new OutputStreamWriter(out)) {
			write(writer, grid);
		}
	}

	/**
	 * Writes the given grid to the specified writer.
	 * 
	 * @param file the writer to write to
	 * @param grid the grid to write
	 * @throws IOException if there is a problem while writing the grid data
	 */
	public static void write(final Writer out, final Grid grid) throws IOException {
		final var lineSeparator = System.getProperty("line.separator");

		for (var row = 1; row <= 9; row++) {
			if (row > 1)
				out.write(lineSeparator);
			for (var column = 1; column <= 9; column++) {
				final var value = grid.get(column, row);
				if (value == 0)
					out.write(EMPTY_CELL);
				else
					out.write(value + 0x30);
			}
		}
	}
}