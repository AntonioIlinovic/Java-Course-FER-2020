package hr.fer.oprpp1.hw04.db;

/**
 * Strategy interface with one method.
 */
public interface IFieldValueGetter {

    /**
     * Method obtains a requested field value from given StudentRecord.
     * @param record obtain a requested field from this student
     * @return requested field from given student
     */
    public String get(StudentRecord record);

}
