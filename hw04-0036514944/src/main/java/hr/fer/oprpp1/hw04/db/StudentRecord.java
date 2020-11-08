package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Instances of this class represent records for each student. Student attributes are: jmbag, lastName, firstName, finalGrade.
 * When started, program reads the data from file database.txt. There must be no duplicate jmbags, or finalGrade that is not
 * between 1 and 5. If there is invalid entry, program should terminate with appropriate message to user.
 * There can not exist multiple records for the same student.
 * Two students are treated as equal if JMBAGs are equal.
 */
public class StudentRecord {

    private String jmbag;
    private String lastName;
    private String firstName;
    private int finalGrade;

    public String getJmbag() {
        return jmbag;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * Constructor for StudentRecord.
     * @param jmbag jmbag
     * @param lastName lastName
     * @param firstName firstName
     * @param finalGrade finalGrade
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
    }

    /**
     * Two students are equal if their jmbags are same.
     * @param o Other object to check if equal to current Student
     * @return if other Object is equal to current Student
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(jmbag, that.jmbag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }

    @Override
    public String toString() {
        return "StudentRecord{" +
                "jmbag='" + jmbag + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", finalGrade=" + finalGrade +
                '}';
    }
}
