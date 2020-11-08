package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class QueryParserTest {

    @Test
    public void query_1() {
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
        assertTrue(qp1.isDirectQuery());
        assertEquals("0123456789", qp1.getQueriedJMBAG());
        assertEquals(1, qp1.getQuery().size());
    }

    @Test
    public void query_2() {
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        assertFalse(qp2.isDirectQuery());
        assertThrows(IllegalStateException.class, qp2::getQueriedJMBAG);
        assertEquals(2, qp2.getQuery().size());
    }

}
