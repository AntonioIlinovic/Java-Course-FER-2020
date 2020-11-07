package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Class which represents a parser of query statement and it gets query string through constructor (actually, it must
 * get everything user entered after query keyword; you must skip this keyword). Inside this class is also a simple
 * lexer which parser uses.
 */
public class QueryParser {


    /**
     * Method must return true if query was of the form jmbag="xxx" (i.e. it must have only one comparison, one
     * attribute jmbag, and operator must be equals). We will call queries of this from direct queries.
     *
     * @return if query was of the form jmbag="xxx"
     */
    public boolean isDirectQuery() {

    }

    /**
     * Method must return the string ("xxx" in method isDirectQuery) which was given in equality comparison in direct
     * query. If the query was not a direct one, method must throw IllegalStateException.
     *
     * @return the string ("xxx" in method isDirectQuery) which was given in equality comparison in direct query
     */
    public String getQueriedJMBAG() {

    }

    /**
     * For all queries, this method must return a list of conditional expressions from query. For direct queries this
     * list will have only one element.
     *
     * @return list of conditional expressions from query
     */
    public List<ConditionalExpression> getQuery() {

    }

}
