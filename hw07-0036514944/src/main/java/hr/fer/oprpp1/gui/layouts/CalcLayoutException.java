package hr.fer.oprpp1.gui.layouts;

/**
 * Exception that is thrown if component is added on positions that are not suitable for CalcLayout or multiple
 * components are added on the same position.
 */
public class CalcLayoutException extends RuntimeException {
    public CalcLayoutException(String message) {
        super(message);
    }
}
