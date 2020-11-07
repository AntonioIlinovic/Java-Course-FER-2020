package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static hr.fer.oprpp1.hw04.db.StudentDatabaseTest.GetOneStudentRecord;
import static org.junit.jupiter.api.Assertions.*;

public class ConditionalExpressionTest {

    @Test
    public void ExpressionTrueTest() {
        StudentRecord record = new StudentRecord(
                "1234567890",
                "Peric",
                "Pero",
                4
        );

        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Per*",
                ComparisonOperators.LIKE
        );

        boolean recordSatisfies = expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(record),
                expr.getStringLiteral()
        );
        assertTrue(recordSatisfies);
    }

    @Test
    public void ExpressionFalseTest() {
        StudentRecord record = new StudentRecord(
                "1234567890",
                "Peric",
                "Pero",
                4
        );

        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Per*ric",
                ComparisonOperators.LIKE
        );

        boolean recordSatisfies = expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(record),
                expr.getStringLiteral()
        );
        assertFalse(recordSatisfies);
    }

}
