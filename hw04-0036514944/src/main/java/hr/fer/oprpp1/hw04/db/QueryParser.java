package hr.fer.oprpp1.hw04.db;

import hr.fer.oprpp1.hw04.db.lexer.QueryLexer;
import hr.fer.oprpp1.hw04.db.lexer.QueryLexerException;
import hr.fer.oprpp1.hw04.db.lexer.QueryLexerTokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents a parser of query statement and it gets query string through constructor (actually, it must
 * get everything user entered after query keyword; you must skip this keyword). Inside this class is also a simple
 * lexer which parser uses.
 */
public class QueryParser {

    /**
     * Lexer (tokenizer) of source code.
     */
    private QueryLexer tokenizer;
    private List<ConditionalExpression> queryList;

    /**
     * Constructor for QueryParser which accepts String of query.
     *
     * @param text to parse
     */
    public QueryParser(String text) {
        if (text == null)
            throw new QueryLexerException("Text of query can't be empty");

        this.tokenizer = new QueryLexer(text);
        try {
            queryList = parse();
        } catch (QueryLexerException e) {
            throw new QueryLexerException("Error occurred in lexing: " + e.getMessage());
        } catch (Exception e) {
            throw new QueryParserException("Error occurred in parsing: " + e.getMessage());
        }
    }

    private List<ConditionalExpression> parse() {
        queryList = new ArrayList<>();

        /* Generate first token at the start of the parsing. */
        tokenizer.nextToken();

        /* Add all nodes in program to DocumentNode and return it. */
        /* EOF: if at the end of program, we are done. */
        while (!isTokenOfType(QueryLexerTokenType.EOF)) {
            /* FIELD_NAME: if at the start of the query, parse it as a query */
            if (isTokenOfType(QueryLexerTokenType.FIELD_NAME)) {
                parseQuery();
                continue;
            }

            /* OPERATOR_AND: if there is an operator AND add another query */
            if (isTokenOfType(QueryLexerTokenType.OPERATOR) &&
                    ((String) tokenizer.getToken().getValue()).equalsIgnoreCase("AND")) {
                tokenizer.nextToken();
                continue;
            }

            throw new QueryLexerException("Error in query syntax");
        }

        return queryList;
    }

    /**
     * Helper method which parses incoming tokens as a query.
     */
    private void parseQuery() {
        /* FIELD_NAME */
        IFieldValueGetter fieldValueGetter;
        String fieldName = (String)tokenizer.getToken().getValue();
        switch (fieldName.toLowerCase()) {
            case "jmbag" -> fieldValueGetter = FieldValueGetters.JMBAG;
            case "firstname" -> fieldValueGetter = FieldValueGetters.FIRST_NAME;
            case "lastname" -> fieldValueGetter = FieldValueGetters.LAST_NAME;
            default -> throw new QueryParserException("Unexpected value: " + fieldName.toLowerCase());
        }
        tokenizer.nextToken();

        /* OPERATOR */
        if (!isTokenOfType(QueryLexerTokenType.OPERATOR))
            throw new QueryParserException("OPERATOR must come after FIELD_NAME");

        IComparisonOperator comparisonOperator;
        String operatorName = (String)tokenizer.getToken().getValue();
        switch (operatorName) {
            case "<" -> comparisonOperator = ComparisonOperators.LESS;
            case "<=" -> comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;
            case ">" -> comparisonOperator = ComparisonOperators.GREATER;
            case ">=" -> comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;
            case "=" -> comparisonOperator = ComparisonOperators.EQUALS;
            case "!=" -> comparisonOperator = ComparisonOperators.NOT_EQUALS;
            case "LIKE" -> comparisonOperator = ComparisonOperators.LIKE;
            default -> throw new QueryParserException("Unexpected value: " + operatorName);
        }
        tokenizer.nextToken();

        /* STRING */
        if (!isTokenOfType(QueryLexerTokenType.STRING))
            throw new QueryParserException("STRING must come after OPERATOR");

        String stringLiteral = (String)tokenizer.getToken().getValue();
        tokenizer.nextToken();


        /* Construct new ConditionalExpression and add it to queryList */
        ConditionalExpression conditionalExpression = new ConditionalExpression(
                fieldValueGetter, stringLiteral, comparisonOperator);
        queryList.add(conditionalExpression);
    }

    /**
     * Helper method which check if current token is of given type.
     *
     * @param type type of token by which it is compared.
     * @return <code>true</code> if it is, <code>false</code> otherwise.
     */
    private boolean isTokenOfType(QueryLexerTokenType type) {
        return tokenizer.getToken().getTokenType() == type;
    }

    /**
     * Method must return true if query was of the form jmbag="xxx" (i.e. it must have only one comparison, one
     * attribute jmbag, and operator must be equals). We will call queries of this from direct queries.
     *
     * @return if query was of the form jmbag="xxx"
     */
    public boolean isDirectQuery() {
        if (queryList.size() != 1)
            return false;

        ConditionalExpression expression = queryList.get(0);

        return expression.getFieldGetter() == FieldValueGetters.JMBAG
                && expression.getComparisonOperator() == ComparisonOperators.EQUALS;
    }

    /**
     * Method must return the string ("xxx" in method isDirectQuery) which was given in equality comparison in direct
     * query. If the query was not a direct one, method must throw IllegalStateException.
     *
     * @return the string ("xxx" in method isDirectQuery) which was given in equality comparison in direct query
     */
    public String getQueriedJMBAG() {
        if (!isDirectQuery())
            throw new IllegalStateException("Query is not direct (not a single query operation)");

        return queryList.get(0).getStringLiteral();
    }

    /**
     * For all queries, this method must return a list of conditional expressions from query. For direct queries this
     * list will have only one element.
     *
     * @return list of conditional expressions from query
     */
    public List<ConditionalExpression> getQuery() {
        return queryList;
    }

}
