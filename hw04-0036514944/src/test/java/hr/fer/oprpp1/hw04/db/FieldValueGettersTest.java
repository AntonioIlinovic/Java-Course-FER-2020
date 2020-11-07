package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static hr.fer.oprpp1.hw04.db.StudentDatabaseTest.GetOneStudentRecord;
import static org.junit.jupiter.api.Assertions.*;


public class FieldValueGettersTest {

    @Test
    public void FIRST_NAMETest() {
        StudentRecord record = GetOneStudentRecord();
        String fieldValue = FieldValueGetters.FIRST_NAME.get(record);
        assertEquals(record.getFirstName(), fieldValue);
    }

    @Test
    public void LAST_NAMETest() {
        StudentRecord record = GetOneStudentRecord();
        String fieldValue = FieldValueGetters.LAST_NAME.get(record);
        assertEquals(record.getLastName(), fieldValue);
    }

    @Test
    public void JMBAGTest() {
        StudentRecord record = GetOneStudentRecord();
        String fieldValue = FieldValueGetters.JMBAG.get(record);
        assertEquals(record.getJmbag(), fieldValue);
    }

}
