package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Smallest unit resulted from Lexical Analysis.
 */
public class SmartScriptToken {

    private final SmartScriptTokenType type;
    private final Object value;

    /**
     * Constructs new {@link SmartScriptToken} depending on {@link SmartScriptTokenType} and value.
     *
     * @param type  of {@link SmartScriptTokenType}
     * @param value of this {@link SmartScriptToken}
     */
    public SmartScriptToken(SmartScriptTokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Getter for Object value.
     *
     * @return Object value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Getter for {@link SmartScriptTokenType} type.
     *
     * @return {@link SmartScriptTokenType} type
     */
    public SmartScriptTokenType getTokenType() {
        return type;
    }

    /**
     * toString method for {@link SmartScriptToken} in desired format.
     *
     * @return SmartScriptToken as a String
     */
    @Override
    public String toString() {
        return "(" + type + ", " + value + ")";
    }
}
