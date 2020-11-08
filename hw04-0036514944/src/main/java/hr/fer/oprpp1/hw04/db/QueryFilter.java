package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Class has a single public constructor which receives one argument: a list of ConditionalExpression objects.
 */
public class QueryFilter implements IFilter {

    List<ConditionalExpression> expressionList;

    /**
     * Constructor which receives one argument: a list of ConditionalExpression objects.
     * @param expressionList list of ConditionalExpression objects
     */
    public QueryFilter(List<ConditionalExpression> expressionList) {
        this.expressionList = expressionList;
    }

    @Override
    public boolean accepts(StudentRecord record) {
        for (ConditionalExpression expression : expressionList) {
            // Check if given StudentRecord satisfies each ConditionalExpression
            boolean recordSatisfiesExpression = expression.getComparisonOperator().satisfied(
                    expression.getFieldGetter().get(record),
                    expression.getStringLiteral()
            );

            // If given StudentRecord doesn't satisfy even one ConditionalExpression it is not acceptable
            if (!recordSatisfiesExpression)
                return false;
        }

        // If given StudentRecord satisfies all ConditionalExpression it is acceptable
        return true;
    }
}
