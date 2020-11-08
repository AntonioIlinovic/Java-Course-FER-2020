package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.oprpp1.hw04.db.StudentDatabaseTest.getDatabaseWithFiveStudentRecords;

public class QueryFilterTest {

    @Test
    public void querySmallDatabaseTest() {
        StudentDatabase database = getDatabaseWithFiveStudentRecords();
        QueryParser parser = new QueryParser("jmbag>=" +
                "\"0000000002\" and lastName<\"J\"");

    }



}
