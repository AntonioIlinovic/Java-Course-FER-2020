package hr.fer.oprpp1.hw04.db;

/**
 * Strategy interface with one method.
 */
public interface IComparisonOperator {

    /**
     * Method checks if two given String literals satisfy some comparison operator.
     * @param value1 first String literal
     * @param value2 second String literal
     * @return if two given String literals satisfy some comparison operator
     */
    public boolean satisfied(String value1, String value2);

}
