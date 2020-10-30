package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits {@link Element}, and has a single read-only String property: symbol.
 */
public class ElementOperator extends Element {

    private final String symbol;

    /**
     * Constructor for {@link ElementOperator}.
     * @param symbol String to initialize {@link ElementOperator} with.
     */
    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Getter for String symbol.
     * @return String symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return Operator in String format.
     */
    @Override
    public String asText() {
        return symbol + " ";
    }
}
