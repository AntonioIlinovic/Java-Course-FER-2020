package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class has internal list of student records. Its constructor must get a list of String objects (the content of
 * database.txt, each String represents one row of the database file). Additionally, it has index for fast retrieval of
 * student records when JMBAG is known (map is used for this). Constructor checks that previously mentioned conditions
 * are satisfied (no duplicate JMBAGs, valid grades from 1 to 5).
 */
public class StudentDatabase {

    List<StudentRecord> studentRecords;
    Map<String, StudentRecord> jmbagIndex;

    /**
     * Constructor for StudentDatabase. It accepts List of Strings from database.txt file representing StudentRecords.
     *
     * @param databaseRows Each String is a row from database.txt file representing one StudentRecord
     */
    public StudentDatabase(List<String> databaseRows) {
        this.studentRecords = new ArrayList<>();
        this.jmbagIndex = new HashMap<>();

        for (String databaseRow : databaseRows) {
            StudentRecord newStudent = RecordFromRow(databaseRow);

            if (newStudent.getFinalGrade() < 1 || newStudent.getFinalGrade() > 5)
                throw new IllegalArgumentException("Student finalGrade must be between 1 and 5. Yours was: " + newStudent.getFinalGrade());
            if (jmbagIndex.containsKey(newStudent.getJmbag()))
                throw new IllegalArgumentException("There is already Student with this JMBAG: " + newStudent.getJmbag());

            studentRecords.add(newStudent);
            jmbagIndex.put(newStudent.getJmbag(), newStudent);
        }
    }

    /**
     * Helper method which accepts one record as a String and returns a new StudentRecord. This method is changed if
     * String record format is changed.
     *
     * @param recordAsString one student record in String format
     * @return new StudentRecord from given String
     */
    private StudentRecord RecordFromRow(String recordAsString) {
        String[] recordAttributes = recordAsString.split("\\t"); // TODO Check how multiple surname entries are split
        if (recordAttributes.length != 4)
            throw new IllegalArgumentException("Each StudentRecord must have 4 attributes: jmbag, lastName, firstName, finalGrade(int 1-5). " +
                    "You entered: '" + recordAsString + "'");

        return new StudentRecord(recordAttributes[0], recordAttributes[1], recordAttributes[2], Integer.parseInt(recordAttributes[3]));
    }

    /**
     * Uses index to obtain requested StudentRecord in O(1). If record does not exist, the method return null.
     *
     * @param jmbag obtain StudentRecord with this JMBAG
     * @return StudentRecord obtained with given JMBAG or null if it doesn't exist
     */
    public StudentRecord forJMBAG(String jmbag) {
        return jmbagIndex.get(jmbag);
    }

    /**
     * Loops through all student records in its internal list. It calls accepts method on given filter-object with
     * current record. Each record for which accepts returns true is added to temporary list and this list is then
     * returned by the filter method.
     *
     * @param filter reference to an object which is an instance of functional interface IFilter
     * @return StudentRecords filtered by given filter
     */
    public List<StudentRecord> filter(IFilter filter) {
        return studentRecords.stream()
                .filter(filter::accepts)
                .collect(Collectors.toList());
    }


}
