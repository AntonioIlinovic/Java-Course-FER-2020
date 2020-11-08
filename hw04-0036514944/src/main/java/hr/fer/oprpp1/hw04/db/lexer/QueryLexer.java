package hr.fer.oprpp1.hw04.db.lexer;

/**
 * Simple Lexer which QueryParser uses. It has only one state.
 */
public class QueryLexer {

    private final char[] data;                  // Input text
    private QueryLexerToken token;              // Last generated token
    private int currentIndex;                   // Index of first non-analyzed character

    /**
     * Constructor for QueryLexer.
     *
     * @param text String to initialize QueryLexer with.
     */
    public QueryLexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
    }

    /**
     * Generates and returns next {@link QueryLexerToken}.
     *
     * @return next generated {@link QueryLexerToken}.
     * @throws QueryLexerException if there is error in lexical analyzation.
     */
    public QueryLexerToken nextToken() {
        /*
        If user requests next token and before that Lexer returned token EOF, an exception is thrown.
         */
        if (token != null && token.getTokenType() == QueryLexerTokenType.EOF)
            throw new QueryLexerException("Requesting next token, but all tokens were exhausted.");

        StringBuilder tokenBuilder = new StringBuilder();   // Used to build next token by appending chars to it.

        // Analyze tokens.
        skipBlanks();
        return analyzeToken(tokenBuilder);
    }

    /**
     * Analyzes data and returns next token.
     * @param tokenBuilder used to build next token by appending chars to id
     * @return newly constructed token
     */
    private QueryLexerToken analyzeToken(StringBuilder tokenBuilder) {
        /*
        If lexer is at the end of text return EOF.
         */
        if (!hasMoreData()) {
            token = new QueryLexerToken(QueryLexerTokenType.EOF, null);
            return token;
        }

        // All OPERATOR Token Type
        if (currentChar() == '=') {
            currentIndex++;
            token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, "=");
            return token;
        }

        if (currentChar() == '<' && nextIthCharEquals('=', 1)) {
            currentIndex += 2;
            token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, "<=");
            return token;
        }

        if (currentChar() == '<') {
            currentIndex++;
            token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, "<");
            return token;
        }

        if (currentChar() == '>' && nextIthCharEquals('=', 1)) {
            currentIndex += 2;
            token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, ">=");
            return token;
        }

        if (currentChar() == '>') {
            currentIndex++;
            token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, ">");
            return token;
        }

        if (currentChar() == '!' && nextIthCharEquals('=', 1)) {
            currentIndex += 2;
            token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, "!=");
            return token;
        }

        if (currentChar() == 'L' &&
                nextIthCharEquals('I', 1) &&
                nextIthCharEquals('K', 2) &&
                nextIthCharEquals('E', 3)) {
            currentIndex += 4;
            token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, "LIKE");
            return token;
        }

        // AND OPERATOR
        if (currentChar() == 'A' || currentChar() == 'a') {
            if (nextIthCharEquals('N', 1) || nextIthCharEquals('n', 1)) {
                if (nextIthCharEquals('D', 2) || nextIthCharEquals('d', 2)) {
                    currentIndex += 3;
                    token = new QueryLexerToken(QueryLexerTokenType.OPERATOR, "AND");
                    return token;
                }
            }
        }

        // STRING Token Type
        if (currentChar() == '"') {
            currentIndex++;

            while (hasMoreData()) {
                if (currentChar() == '"')
                    break;

                tokenBuilder.append(currentChar());

                currentIndex++;
            }

            // If String was properly closed
            if (hasMoreData() && currentChar() == '"') {
                currentIndex++;
                token = new QueryLexerToken(QueryLexerTokenType.STRING, tokenBuilder.toString());
                return token;
            } else {
                throw new QueryLexerException("String wasn't closed properly");
            }
        }

        // FIELD_NAME Token Type
        while (hasMoreData() && Character.isAlphabetic(currentChar())) {
            tokenBuilder.append(currentChar());
            currentIndex++;
        }
        token = new QueryLexerToken(QueryLexerTokenType.FIELD_NAME, tokenBuilder.toString());
        return token;
    }


    /**
     * Returns last generated {@link QueryLexerToken}. Can be called multiple times. Does not generate next {@link
     * QueryLexerToken}.
     *
     * @return last generated {@link QueryLexerToken}.
     */
    public QueryLexerToken getToken() {
        return token;
    }

    /**
     * Helper method which checks if there is data i places after current index and tells you if it is equal to its
     * parameter. Index of character is returned to place when the method was called.
     *
     * @param character    to check if it is equal to the current character.
     * @param ithNextIndex position of character to check.
     * @return if ith next character is equal to the given character.
     */
    private boolean nextIthCharEquals(char character, int ithNextIndex) {
        currentIndex += ithNextIndex;

        if (hasMoreData() && currentChar() == character) {
            currentIndex -= ithNextIndex;
            return true;
        }

        currentIndex -= ithNextIndex;
        return false;
    }

    /**
     * Helper method which returns current char analyzed.
     *
     * @return char which is currently analyzed.
     */
    private char currentChar() {
        return data[currentIndex];
    }

    /**
     * Helper method which checks if there is more data for Lexer to read.
     *
     * @return if has more data to read.
     */
    private boolean hasMoreData() {
        return currentIndex < data.length;
    }

    /**
     * Method moves currentIndex so that it skips all empty characters (spaces, next line, tabulators).
     */
    private void skipBlanks() {
        while (currentIndex < data.length) {
            char c = data[currentIndex];
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                currentIndex++;
                continue;
            }
            break;
        }
    }

}
