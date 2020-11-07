package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

public class QueryParserTest {

    @Test
    public void query_1() {
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
        System.out.println("isDirectQuery(): " + qp1.isDirectQuery()); // true
        System.out.println("jmbag was: " + qp1.getQueriedJMBAG()); // 0123456789
        System.out.println("size: " + qp1.getQuery().size()); // 1
    }

    @Test
    public void query_2() {
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        System.out.println("isDirectQuery(): " + qp2.isDirectQuery()); // false
        // System.out.println(qp2.getQueriedJMBAG()); // would throw!
        System.out.println("size: " + qp2.getQuery().size()); // 2
    }

}
