package hr.fer.oprpp1.hw02.prob1;

import java.text.ParseException;

/**
 * Simple lexical analyzer.
 */
public class Lexer {

    /*
    Pravila:

    Tekst se sastoji od niza riječi, brojeva te simbola. Riječ je svaki niz od jednog ili više znakova nad kojima
    metoda Character.isLetter vraća true. Broj je svaki niz od jedne ili više znamenke, a koji je prikaziv u tipu
    Long. Zabranjeno je u tekstu imati prikazan broj koji ne bi bio prikaziv tipom Long
    (u tom slučaju lexer mora baciti iznimku: ulaz ne valja!). Simbol je svaki pojedinačni znak koji se dobije
    kada se iz ulaznog teksta uklone sve riječi i brojevi te sve praznine ('\r', '\n', '\t', ' ').
    Praznine ne generiraju nikakve tokene (zanemaruju se). Vrijednosti koje su riječi pohranjuju se kao primjerci
    razreda String, brojevi kao primjerci razreda Long, a simboli kao primjerci razreda Character.
    Token tipa EOF generira se kao posljednji token u obradi, nakon što lexer grupira sve znakove iz ulaza i bude
    ponovno pozvan da obavi daljnje grupiranje. U slučaju da korisnik nakon što lexer vrati token tipa EOF opet
    zatraži generiranje sljedećeg tokena, lexer treba baciti iznimku.

    Dopšta se uporaba mehanizma escapeanja: ako se u tekstu ispred znaka koji je znamenka nađe znak \,
    ta se znamenka tretira kao slovo.
     */

    private final char[] data;      // Input text
    private Token token;            // Current token
    private int currentIndex;       // Index of first non-analyzed character
    private LexerState state;       // State of a lexer, can be BASIC and EXTENDED

    /**
     * Constructor with program text.
     *
     * @param text which will be lexically tokenized.
     */
    public Lexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
        state = LexerState.BASIC;
    }

    /**
     * Generates and returns next {@link Token}.
     *
     * @return next {@link Token}.
     * @throws LexerException if there is error in lexical analyzation.
     */
    public Token nextToken() {
        /*
        If user after lexer returned token EOF requests next token, an exception is thrown.
         */
        if (token != null && token.getType() == TokenType.EOF)
            throw new LexerException("Requesting next token, but all tokens were exhausted.");

        skipBlanks();

        boolean escapedChar = false;        // Boolean used to check if next char will be escaped and treated like a letter.
        StringBuilder tokenBuilder = new StringBuilder();   // Used to build next token by appending chars to it.

        /*
        If lexer is at the end of text return EOF.
         */
        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }

        /*
        If char is '#' toggle to other LexerState and return SYMBOL token.
         */
        if (data[currentIndex] == '#') {
            currentIndex++;

            if (state == LexerState.BASIC)
                state = LexerState.EXTENDED;
            else
                state = LexerState.BASIC;

            token = new Token(TokenType.SYMBOL, '#');
            return token;
        }

        /*
        If Lexer is in EXTENDED state.
         */
        if (state == LexerState.EXTENDED) {
            skipBlanks();

            /*
            All chars are treated as WORD token. '#' char will toggle state. ' ' blank space will splits the WORD in EXTENDED state.
             */
            while (currentIndex < data.length && data[currentIndex] != '#' && data[currentIndex] != ' ') {
                tokenBuilder.append(data[currentIndex++]);
            }

            token = new Token(TokenType.WORD, tokenBuilder.toString());
            return token;
        }

        /*
        If token starts with letter or escape sign token will be of type WORD.
         */
        if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
            if (data[currentIndex] == '\\')
                escapedChar = true;

            while (currentIndex < data.length &&
                    (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\')) {
                if (escapedChar) {
                    escapedChar = false;
                    currentIndex++;
                    if (currentIndex >= data.length) // Case when escape char '\' is at the end of the text.
                        throw new LexerException("Error in escaping characters in Lexical Analysis.");
                    if (Character.isDigit(data[currentIndex]))
                        tokenBuilder.append(data[currentIndex++]);
                    else if (data[currentIndex] == '\\') {
                        tokenBuilder.append("\\");
                        currentIndex++;
                    } else
                        throw new LexerException("Error in escaping characters in Lexical Analysis.");
                } else {
                    if (data[currentIndex] == '\\')
                        escapedChar = true;
                    else
                        tokenBuilder.append(data[currentIndex++]);
                }
            }

            token = new Token(TokenType.WORD, tokenBuilder.toString());
            return token;
        }

        /*
        If token starts with digit, token will be of type NUMBER.
         */
        if (Character.isDigit(data[currentIndex])) {
            while (currentIndex < data.length &&
                    Character.isDigit(data[currentIndex])) {
                tokenBuilder.append(data[currentIndex++]);
            }

            try {
                token = new Token(TokenType.NUMBER, Long.parseLong(tokenBuilder.toString()));
            } catch (NumberFormatException ex) {
                throw new LexerException("Number can not be shown in Long format.");
            }
            return token;
        }

        /*
        else token is a SYMBOL.
         */

        token = new Token(TokenType.SYMBOL, data[currentIndex++]);
        return token;
    }

    /**
     * Returns last generated {@link Token}. Can be called multiple times. Does not generate next {@link Token}.
     *
     * @return last generated {@link Token}.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Set state of a lexer, can be BASIC and EXTENDED. In BASIC state lexer works analyzing every letter by itself. In
     * EXTENDED state lexer tokens are separated by blanks, and every symbols, letters and digits are treated as a
     * WORD.
     *
     * @param state to put lexer in.
     */
    public void setState(LexerState state) {
        if (state == null)
            throw new NullPointerException("Lexer state can't be null.");
        this.state = state;
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
