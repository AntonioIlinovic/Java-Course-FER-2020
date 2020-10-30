package hr.fer.oprpp1.hw02.prob1;

/**
 * Smallest unit resulted from Lexical Analysis.
 */
public class Token {

    private final TokenType type;
    private final Object value;

    /**
     * Constructs new {@link Token} depending on {@link TokenType} and value.
     * @param type of {@link Token}.
     * @param value of {@link Token}.
     */
    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Getter for Object value.
     * @return value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Getter for TokenType type.
     * @return TokenType type
     */
    public TokenType getType() {
        return type;
    }

    /**
     * toString method for Token.
     * @return Token formatted as a String
     */
    @Override
    public String toString() {
        return "(" + type + ", " + value + ")";
    }
}
