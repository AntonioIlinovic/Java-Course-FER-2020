package hr.fer.oprpp1.hw05.shell;

/**
 * Subclass of RuntimeException. If read or writing in MyShell fails, this exception is thrown.
 */
public class ShellIOException extends RuntimeException {

    public ShellIOException() { super(); }

    public ShellIOException(String message) {
        super(message);
    }

}
