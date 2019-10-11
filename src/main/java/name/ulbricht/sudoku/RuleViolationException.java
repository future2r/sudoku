package name.ulbricht.sudoku;

/**
 * This exception is thrown if an operation would violate the Sudoku rules.
 */
public class RuleViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuleViolationException() {
	}

	public RuleViolationException(String message) {
		super(message);
	}

	public RuleViolationException(Throwable cause) {
		super(cause);
	}

	public RuleViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuleViolationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}