package hr.fer.oprpp1.hw04.db;


/**
 * {@link QueryParserException} is thrown when there is error in tokenization.
 */
public class QueryParserException extends RuntimeException {
    public QueryParserException(String message) {
        super(message);
    }
}
