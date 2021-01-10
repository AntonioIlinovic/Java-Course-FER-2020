package hr.fer.oprpp1.gui.layouts;

import java.util.Objects;

/**
 * Class with two read-only properties: row and column (of type int). Class has public static method factory which
 * accepts textual specification (such as "3,7") and returns appropriate object RCPosition.
 */
public class RCPosition {

    /* Row and Column values, there is no restriction on row and col values */
    private final int row;
    private final int col;

    /**
     * RCPosition constructor with two arguments: row and column.
     *
     * @param row of component
     * @param col of component
     */
    public RCPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Getter for row value.
     *
     * @return row value
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for column value.
     *
     * @return column value
     */
    public int getCol() {
        return col;
    }

    /**
     * Another way to get new object RCPosition. We can give give it text and it will parse and return a new Object of
     * RCPosition type. Text needs to be in format: "2,4"  "1,7"  "4,3"  "-3,10"  etc...
     *
     * @param text from which to parse new RCPosition
     * @return new RCPosition object parsed from given text
     */
    public static RCPosition parse(String text) {
        String[] dimensions = text.split(",");

        if (dimensions.length != 2)
            throw new IllegalArgumentException("RCPosition parse method accepts String with two integers separated by comma. You entered: " + text);

        int parsedRow;
        int parsedCol;

        try {
            parsedRow = Integer.parseInt(dimensions[0]);
            parsedCol = Integer.parseInt(dimensions[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("RCPosition parse method accepts String with two integers separated by comma. You entered: " + text);
        }

        return new RCPosition(parsedRow, parsedCol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RCPosition that = (RCPosition) o;
        return row == that.row &&
                col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "RCPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
