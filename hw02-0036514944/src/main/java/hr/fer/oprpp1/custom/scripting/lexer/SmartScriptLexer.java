package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Lexer for smart script. This format will have text and tags. Each tag has its name. The name of '{$FOR$}' is FOR.
 * Tags are in form: '{$ ... $}'. Spaces in tags are ignorable ('{$END$}' is same as '{$  END  $}'. Tag names are case
 * insensitive ('{$FOR$}' is same as '{$for$}' or '{$For$}'. '{$= ...$}' tag is an empty tag, and it doesn't need
 * closing tag. FOR tag for example needs a closing tag ('{$END$}'). This class will produce tokens, which will be used
 * by SmartScriptParser.
 *
 * <b>Valid variable name</b> starts by letter and after follows zero or more letters, digits or underscores. If name
 * is not valid, it is invalid. This variable names are valid: A7_bb, counter, tmp_34; these are not: _a21, 32, 3s_ee
 * etc.
 *
 * <b>Valid function name</b> starts with @ after which follows a letter and after than can follow zero or more
 * letters, digits or underscores. If function name is not valid, it is invalid.
 *
 * <b>Valid operators</b> are + (plus), - (minus), * (multiplication), / (division), ^ (power)
 *
 * <b>Valid tag names</b> are “=”, or variable name. So = is valid tag name (but not valid variable name).
 * <p>
 * <p>
 * Lexer should extract as many characters as possible into each token. Spaces between tokens are ignorable and
 * generally not required. Decimal numbers are only recognized in digits-dot-digits format, but not in scientific
 * notation. The consequence of this is that tag: '{$ FOR i-1.35bbb"1" $}' is correct, and is tokenized the same way as
 * if the following was given: '{$ FOR i -1.35 bbb "1" $}'.
 * <p>
 * In Lexer, when deciding what to do with minus sign, treat is as a symbol if immediately after it there is no digit.
 * Only when immediately after it (no spaces between) a digit follow (Lexer can check this!), treat it as part of
 * negative number.
 *
 *
 *
 * <b>Escaping explained</b>:
 * <p>
 * In strings which are part of tags (and only in strings!) parser must accept the following escaping: \\ sequence treat
 * as a single string character \ \" treat as a single string character " (and not the end of the string) \n, \r and \t
 * have its usual meaning (ascii 10, 13 and 9). Every other sequence which starts with \ should be treated as invalid
 * and throw an exception. For example, "Some \\ test X" should be interpreted as string with value Some \ test X.
 * Another example: "Joe \"Long\" Smith" represents a single string with value Joe "Long" Smith. Please note: string
 * "Some \\ test X" shown here is what user would write in his document, which will be analyzed. If you wish to write it
 * in Java source code (e.g. for testing purposes), you will have to escape each “ and each \. Here is example:
 * <p>
 * String simpleDoc = "A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}."
 * <p>
 * which will represent document: A tag follows {$= "Joe \"Long\" Smith"$}.
 * <p>
 * In document text (i.e. outside of tags) parser must accept only the following two escaping: \\ treat as \ \{ treat as
 * { Every other sequence which starts with \ should throw an exception. Please note, in this context, character { is
 * just a regular character if it is not followed directly by $. So the next document is just a single text:
 * <p>
 * Example { bla } blu \{$=1$}. Nothing interesting {=here}.
 * <p>
 * <p>
 * For example, document whose content is following:
 * <p>
 * Example \{$=1$}. Now actually write one {$=1$}
 * <p>
 * should be parsed into only three nodes:
 * <p>
 * DocumentNode * *- TextNode with value Example {$=1$}. Now actually write one *- EchoNode with one element
 */
public class SmartScriptLexer {

    private final char[] data;                  // Input text
    private SmartScriptToken token;             // Last generated token
    private int currentIndex;                   // Index of first non-analyzed character
    private SmartScriptLexerState state;        // State of a lexer

    /**
     * Construtor for {@link SmartScriptLexer}.
     *
     * @param text String to initialize {@link SmartScriptLexer} with.
     */
    public SmartScriptLexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
        state = SmartScriptLexerState.TEXT;
    }


    /**
     * Generates and returns next {@link SmartScriptToken}.
     *
     * @return next generated {@link SmartScriptToken}.
     * @throws SmartScriptLexerException if there is error in lexical analyzation.
     */
    public SmartScriptToken nextToken() {
        /*
        If user requests next token and before that Lexer returned token EOF, an exception is thrown.
         */
        if (token != null && token.getTokenType() == SmartScriptTokenType.EOF)
            throw new SmartScriptLexerException("Requesting next token, but all tokens were exhausted.");

        StringBuilder tokenBuilder = new StringBuilder();   // Used to build next token by appending chars to it.

        /*
        If lexer is at the end of text return EOF.
         */
        if (!hasMoreData()) {
            token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
            return token;
        }

        // Depending in which state Lexer is, analyze that type of tokens.
        if (state == SmartScriptLexerState.TEXT) // TEXT state
            return lexerText(tokenBuilder);
        else // TAG state
        {
            skipBlanks();
            return lexerTag(tokenBuilder);
        }
    }

    /* Analyze tokens in TEXT state. */
    private SmartScriptToken lexerText(StringBuilder tokenBuilder) {
        /*
        First check if TAGSTART is at the beginning. If it is, return TAGSTART token and switch to TAG state.
        Implemented so it checks if current character is '{'. If it is check if next character is '$'.
        If then first 2 characters are '{$' return TAGSTART. Otherwise go back at index when you entered this method
        and analyze it as a STRING.
         */
        if (hasMoreData() && currentChar() == '{') {
            if (nextIthCharEquals('$', 1)) {
                currentIndex += 2;      // Move index 2 places because length of "{$" is equal to 2
                token = new SmartScriptToken(SmartScriptTokenType.TAGSTART, "{$");
                return token;
            }
        }

        /*
        In TEXT state parser must accept only the following two escaping:
        \\ treat as \
        \{ treat as {
        Every other sequence which starts with \ should throw an exception.
         */
        boolean escaped = false;        // If last char was escape char '\'


        /* Go through text and build token until you find TAGSTART '{$' */
        while (hasMoreData()) {
            /* First check if last char was escape char '\' */
            if (escaped) {
                escaped = false;

                if (currentChar() == '\\' || currentChar() == '{') {
                    tokenBuilder.append(currentChar());
                    currentIndex++;
                } else {
                    throw new SmartScriptLexerException("Escaping error, you can't have " + currentChar() + " as escaped character in TEXT mode");
                }
            } else {
                /* If it is not escaped first check for TAGSTART. */
                if (currentChar() == '{') {
                    if (nextIthCharEquals('$', 1)) {
                        break;
                    }
                }

                if (currentChar() == '\\') {
                    escaped = true;
                } else {
                    tokenBuilder.append(currentChar());
                }
                currentIndex++;
            }
        }

        // If there is no data, but last character was escape character
        if (escaped)
            throw new SmartScriptLexerException("Escaping error, you can't finish your text with just an escape character '\\'");


        token = new SmartScriptToken(SmartScriptTokenType.TEXT, tokenBuilder.toString());
        return token;
    }


    /* Analyze tokens in TAG state. */
    private SmartScriptToken lexerTag(StringBuilder tokenBuilder) {
        // TAGEND tag
        if (currentChar() == '$') {
            if (nextIthCharEquals('}', 1)) {
                currentIndex += 2;      // Move index 2 places because length of "{$" is equal to 2
                token = new SmartScriptToken(SmartScriptTokenType.TAGEND, "$}");
                return token;
            }
        }

        // FUNCTION tag
        /*
        Valid function name starts with @ after which follows a letter and after than can follow zero or more letters,
        digits or underscores. If function name is not valid, it is invalid.
         */
        if (currentChar() == '@') {
            currentIndex++;

            if (hasMoreData() && Character.isLetter(currentChar())) {
                tokenBuilder.append(currentChar());
                currentIndex++;
            } else {
                throw new SmartScriptLexerException("Function name must start with a letter");
            }

            while (hasMoreData() &&
                    (Character.isLetter(currentChar())) || Character.isDigit(currentChar()) || currentChar() == '_') {
                tokenBuilder.append(currentChar());
                currentIndex++;
            }

            token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, tokenBuilder.toString());
            return token;
        }

        // OPERATOR tag
        if (currentChar() == '+' || currentChar() == '-' || currentChar() == '*' || currentChar() == '/' || currentChar() == '^') {
            if (currentChar() == '-') {
                currentIndex++;
                if (hasMoreData()) {
                    if (!Character.isDigit(currentChar())) {
                        // If digit doesn't follow after '-' sign, treat it as a OPERATOR, number otherwise.
                        currentIndex--;
                        token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, "-");
                        return token;
                    }
                }
                currentIndex--;
            }

            if (currentChar() == '+') {
                currentIndex++;
                token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, "+");
                return token;
            }

            if (currentChar() == '*') {
                currentIndex++;
                token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, "*");
                return token;
            }

            if (currentChar() == '/') {
                currentIndex++;
                token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, "/");
                return token;
            }

            if (currentChar() == '^') {
                currentIndex++;
                token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, "^");
                return token;
            }
        }

        // STRING tag
        /*
        In strings which are part of tags (and only in strings!) parser must accept the following escaping:
        \\ sequence treat as a single string character \
        \" treat as a single string character " (and not the end of the string)
        \n, \r and \t have its usual meaning (ascii 10, 13 and 9).
        Every other sequence which starts with \ should be treated as invalid and throw an exception
         */
        if (currentChar() == '"') {
            currentIndex++;

            boolean escaped = false;        // If last char was escape char '\'

            while (hasMoreData()) {
                if (escaped) {
                    escaped = false;

                    // TODO Convert if, else if, else to switch
                    if (currentChar() == '\\')
                        tokenBuilder.append("\\");
                    else if (currentChar() == '"')
                        tokenBuilder.append("\"");
                    else if (currentChar() == 'n')
                        tokenBuilder.append("\n");
                    else if (currentChar() == 't')
                        tokenBuilder.append("\t");
                    else if (currentChar() == 'r')
                        tokenBuilder.append("\r");
                    else
                        throw new SmartScriptLexerException("Error in escaping, you can't escape character " + currentChar());
                    currentIndex++;
                } else {
                    if (currentChar() == '"') {
                        /* If there is another character '"' which is not escaped, stop building the String. */
                        break;
                    }
                    if (currentChar() == '\\') {
                        escaped = true;
                    } else {
                        tokenBuilder.append(currentChar());
                    }

                    currentIndex++;
                }
            }

            // If String was properly closed
            if (hasMoreData() && currentChar() == '"') {
                currentIndex++;
                token = new SmartScriptToken(SmartScriptTokenType.STRING, tokenBuilder.toString());
                return token;
            } else {
                throw new SmartScriptLexerException("String wasn't closed properly");
            }
        }

        // TAGNAME tag
        /*
        TAGNAME can be '=', 'FOR' or 'END' (ignoring case)
         */
        if (currentChar() == '=') {
            currentIndex++;
            token = new SmartScriptToken(SmartScriptTokenType.TAGNAME, "=");
            return token;
        } else if (currentChar() == 'f' || currentChar() == 'F') {
            if (nextIthCharEquals('o', 1) || nextIthCharEquals('O', 1)) {
                if (nextIthCharEquals('r', 2) || nextIthCharEquals('R', 2)) {
                    currentIndex += 3;
                    token = new SmartScriptToken(SmartScriptTokenType.TAGNAME, "FOR");
                    return token;
                }
            }
        } else if (currentChar() == 'e' || currentChar() == 'E') {
            if (nextIthCharEquals('n', 1) || nextIthCharEquals('N', 1)) {
                if (nextIthCharEquals('d', 2) || nextIthCharEquals('D', 2)) {
                    currentIndex += 3;
                    token = new SmartScriptToken(SmartScriptTokenType.TAGNAME, "END");
                    return token;
                }
            }
        }

        // VARIABLE tag
        /*
        Valid variable name starts by letter and after follows zero or more letters, digits or underscores. If name is
        not valid, it is invalid. This variable names are valid: A7_bb, counter, tmp_34; these are not: _a21, 32,
        3s_ee etc.
         */
        if (Character.isLetter(currentChar())) {
            tokenBuilder.append(currentChar());
            currentIndex++;

            while (hasMoreData() &&
                    (Character.isLetter(currentChar()) || Character.isDigit(currentChar()) || currentChar() == '_')) {
                tokenBuilder.append(currentChar());
                currentIndex++;
            }

            token = new SmartScriptToken(SmartScriptTokenType.VARIABLE, tokenBuilder.toString());
            return token;
        }

        // DOUBLE or INTEGER tag
        /*
        If it starts with '-', it is a number because we already checked that it is not an operator.
        Double number is only valid in digit-dot-digit form.
         */
        boolean dotRead = false;
        // First character can be '-'
        if (currentChar() == '-') {
            tokenBuilder.append(currentChar());
            currentIndex++;
        }
        // Only digit can be first character, not counting '-'
        if (hasMoreData() && Character.isDigit(currentChar())) {
            tokenBuilder.append(currentChar());
            currentIndex++;
        }
        // Now character can be digit or '.'
        while (hasMoreData() && (Character.isDigit(currentChar()) || currentChar() == '.')) {
            if (currentChar() == '.') {
                dotRead = true;
                tokenBuilder.append(currentChar());
                currentIndex++;
                break;
            } else {
                tokenBuilder.append(currentChar());
                currentIndex++;
            }
        }
        // Now only digits are allowed
        while (hasMoreData() && Character.isDigit(currentChar())) {
            tokenBuilder.append(currentChar());
            currentIndex++;
        }

        // If dot was read token is DOUBLE, otherwise INTEGER
        if (dotRead) {
            token = new SmartScriptToken(SmartScriptTokenType.DOUBLE, tokenBuilder.toString());
        } else {
            token = new SmartScriptToken(SmartScriptTokenType.INTEGER, tokenBuilder.toString());
        }

        return token;
    }


    /**
     * Returns last generated {@link SmartScriptToken}. Can be called multiple times. Does not generate next {@link
     * SmartScriptToken}.
     *
     * @return last generated {@link SmartScriptToken}.
     */
    public SmartScriptToken getToken() {
        return token;
    }

    /**
     * Set state of a lexer, can be TEXT and TAG. In TEXT state Lexer works analyzing everything as a text. In TAG state
     * Lexer rules for TAG apply.
     *
     * @param state to put lexer in.
     */
    public void setState(SmartScriptLexerState state) {
        if (state == null)
            throw new NullPointerException("Lexer state can't be null.");
        this.state = state;
    }

    /**
     * Lexer state getter. Can be TEXT and TAG state.
     *
     * @return state of lexer.
     */
    public SmartScriptLexerState getState() {
        return state;
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
     * @return char which is currently analzyed.
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
