package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Types of Tokens for SmartScript Lexer.
 */
public enum SmartScriptTokenType {

    // Signifies there is no more tokens
    EOF,
    // Text
    TEXT,
    // Keyword: (FOR, END, =)
    TAGNAME,
    // Variable
    VARIABLE,
    // Function
    FUNCTION,
    // Operator
    OPERATOR,
    // Integer
    INTEGER,
    // Double
    DOUBLE,
    // String
    STRING,
    // Signifies start of a tag
    TAGSTART,
    // Signifies end of a tag
    TAGEND

}
