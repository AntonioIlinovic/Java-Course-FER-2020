package hr.fer.oprpp1.hw04.db.lexer;

/**
 * Types of Tokens for QueryLexer.
 */
public enum QueryLexerTokenType {

    /** Signifies there is no more tokens **/
    EOF,
    /** Operator such as: >, <, >=, <=, =, !=, LIKE **/
    OPERATOR,
    /** String in quotation marks "..." **/
    STRING,
    /** FieldName such as: StudentRecord's firstName, lastName, jmbag, finalGrade **/
    FIELD_NAME

}
