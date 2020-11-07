package hr.fer.oprpp1.hw04.db.lexer;

/**
 * Smallest unit resulted from Lexical Analysis.
 */
public class QueryLexerToken {

    private final QueryLexerTokenType type;
    private final Object value;

    /**
     * Constructs new QueryLexerToken depending on given values.
     * @param type of QueryLexerToken
     * @param value of QueryLexerToken
     */
    public QueryLexerToken(QueryLexerTokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Getter for Object value.
     * @return Object value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Getter for QueryLexerToken type.
     * @return QueryLexerToken type.
     */
    public QueryLexerTokenType getTokenType() {
        return type;
    }

    /**
     * toString method for QueryLexerToken in desired format.
     * @return QueryLexerToken as a String.
     */
    @Override
    public String toString() {
        return "(" + type + ", " + value + ")";
    }
}
