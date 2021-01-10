package hr.fer.oprpp1.custom.scripting.parser;

/**
 * {@link SmartScriptParserException} is thrown when there is error in parsing.
 */
public class SmartScriptParserException extends RuntimeException {
    public SmartScriptParserException(String message) {
        super(message);
    }
}
