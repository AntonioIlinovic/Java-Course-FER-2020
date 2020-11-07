package hr.fer.oprpp1.hw04.db;

/**
 * Functional interface.
 */
@FunctionalInterface
public interface IFilter {

    /**
     * Method accepts a StudentRecord and returns if it is acceptable or not.
     *
     * @param record StudentRecord
     * @return if given record is acceptable or not
     */
    boolean accepts(StudentRecord record);

}
