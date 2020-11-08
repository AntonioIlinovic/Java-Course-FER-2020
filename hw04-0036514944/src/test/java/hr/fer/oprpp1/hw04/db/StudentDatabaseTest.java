package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class StudentDatabaseTest {

    static StudentDatabase getDatabaseWithFiveStudentRecords() {
        List<String> studentAsStrings = List.of(new String[]{
                "0000000001	Akšamović	Marin	2",
                "0000000002	Bakamović	Petra	3",
                "0000000003	Bosnić	Andrea	4",
                "0000000004	Božić	Marin	5",
                "0000000005	Brezović	Jusufadis	2"
        });

        return new StudentDatabase(studentAsStrings);
    }

    static StudentRecord GetOneStudentRecord() {
        return new StudentRecord(
                "1234567890",
                "Doe",
                "John",
                4
        );
    }

    @Test
    public void forJMBAGTest() {
        StudentDatabase studentDatabase = getDatabaseWithFiveStudentRecords();
        StudentRecord expectedStudent = new StudentRecord("0000000003","Bosnić","Andrea",4);
        assertEquals(expectedStudent, studentDatabase.forJMBAG("0000000003"));
        assertNull(studentDatabase.forJMBAG("0123456789"));
    }

    @Test
    public void filterReturnsAllRecords() {
        StudentDatabase studentDatabase = getDatabaseWithFiveStudentRecords();

        List<StudentRecord> filteredStudents = studentDatabase.filter(record -> true);

        assertEquals(studentDatabase.studentRecords, filteredStudents);
    }

    @Test
    public void filterReturnsNoRecords() {
        StudentDatabase studentDatabase = getDatabaseWithFiveStudentRecords();

        List<StudentRecord> filteredStudents = studentDatabase.filter(record -> false);

        assertEquals(0, filteredStudents.size());
    }

}
