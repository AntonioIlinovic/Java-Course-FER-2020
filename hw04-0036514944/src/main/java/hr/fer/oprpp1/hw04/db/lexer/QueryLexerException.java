package hr.fer.oprpp1.hw04.db.lexer;

/**
 * QueryLexerException is thrown when there is error in tokenization.
 */
public class QueryLexerException extends RuntimeException {
    public QueryLexerException(String message) {
        super(message);
    }
}
