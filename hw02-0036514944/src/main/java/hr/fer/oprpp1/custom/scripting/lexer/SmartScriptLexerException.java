package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * {@link SmartScriptLexerException} is thrown when there is error in tokenization.
 */
public class SmartScriptLexerException extends RuntimeException {
    public SmartScriptLexerException(String message) {
        super(message);
    }
}
