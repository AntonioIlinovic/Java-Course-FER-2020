package hr.fer.oprpp1.hw04.db;

/**
 * Class with static concrete strategies for abstract strategy IComparisonOperator.
 */
public class ComparisonOperators {

    public static final IComparisonOperator LESS = (s1, s2) -> s1.compareTo(s2) < 0;
    public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) <= 0;
    public static final IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0;
    public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) >= 0;
    public static final IComparisonOperator EQUALS = (s1, s2) -> s1.compareTo(s2) == 0;
    public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> s1.compareTo(s2) != 0;

    public static final IComparisonOperator LIKE = (value, format) -> {
        // If user enters more wildcard characters * than one, throw an exception
        long wildcardOccurrences = format.chars()
                .filter(ch -> ch == '*')
                .count();
        if (wildcardOccurrences > 1)
            throw new IllegalArgumentException("Wildcard character * can be entered 0 or 1 time, you entered it: " + wildcardOccurrences + " times");

        if (wildcardOccurrences == 0)
            return value.equals(format);

        int wildcardIndex = format.indexOf('*');
        String mustStartWith = format.substring(0, wildcardIndex);
        String mustEndWith = format.substring(wildcardIndex+1, format.length());

        if (wildcardIndex == 0) {                           // If * is at the start
            return value.endsWith(mustEndWith);
        } else if (wildcardIndex == format.length() - 1) {    // If * is at the end
            return value.startsWith(mustStartWith);
        }
        /* If * is in the middle
        "AAA" LIKE "AA*AA" -> false
        "AAAA" LIKE "AA*AA" -> true
         */
        if (value.length() < mustStartWith.length() + mustEndWith.length())
            return false;

        return (value.startsWith(mustStartWith) && value.endsWith(mustEndWith));
    };

}
