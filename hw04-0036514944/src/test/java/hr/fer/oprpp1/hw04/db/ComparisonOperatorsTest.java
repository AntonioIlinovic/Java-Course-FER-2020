package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorsTest {

    @Test
    public void LESSTest() {
        IComparisonOperator oper = ComparisonOperators.LESS;
        assertTrue(oper.satisfied("Ana", "Jasna"));
        assertFalse(oper.satisfied("Jasna", "Ana"));
        assertFalse(oper.satisfied("Ana", "Ana"));
    }

    @Test
    public void LESS_OR_EQUALSTest() {
        IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
        assertTrue(oper.satisfied("Ana", "Jasna"));
        assertFalse(oper.satisfied("Jasna", "Ana"));
        assertTrue(oper.satisfied("Ana", "Ana"));
    }

    @Test
    public void GREATERTest() {
        IComparisonOperator oper = ComparisonOperators.GREATER;
        assertFalse(oper.satisfied("Ana", "Jasna"));
        assertTrue(oper.satisfied("Jasna", "Ana"));
        assertFalse(oper.satisfied("Ana", "Ana"));
    }
    @Test

    public void GREATER_OR_EQUALSTest() {
        IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
        assertFalse(oper.satisfied("Ana", "Jasna"));
        assertTrue(oper.satisfied("Jasna", "Ana"));
        assertTrue(oper.satisfied("Ana", "Ana"));
    }

    @Test
    public void EQUALSTest() {
        IComparisonOperator oper = ComparisonOperators.EQUALS;
        assertFalse(oper.satisfied("Ana", "Jasna"));
        assertFalse(oper.satisfied("Jasna", "Ana"));
        assertTrue(oper.satisfied("Ana", "Ana"));
    }

    @Test
    public void NOT_EQUALSTest() {
        IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
        assertTrue(oper.satisfied("Ana", "Jasna"));
        assertTrue(oper.satisfied("Jasna", "Ana"));
        assertFalse(oper.satisfied("Ana", "Ana"));
    }

    @Test
    public void LIKETest() {
        IComparisonOperator oper = ComparisonOperators.LIKE;
        assertFalse(oper.satisfied("Zagreb", "Aba*"));
        assertFalse(oper.satisfied("AAA", "AA*AA"));
        assertTrue(oper.satisfied("AAAA", "AA*AA"));
        assertTrue(oper.satisfied("AABB", "*B"));
        assertTrue(oper.satisfied("AABB", "A*"));
        assertThrows(IllegalArgumentException.class, () -> oper.satisfied("AAAA", "AA**AA"));
    }
}
