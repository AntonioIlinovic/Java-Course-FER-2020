package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * Environment interface which is an abstraction which will be passed to each defined command. The each implemented
 * command communicates with user (reads user input and writes response) only through this interface.
 */
public interface Environment {

    /**
     * Reading text from user.
     *
     * @return text read from user
     * @throws ShellIOException if reading a line fails
     */
    String readLine() throws ShellIOException;

    /**
     * Writing to user (console) given text.
     *
     * @param text to write to user
     * @throws ShellIOException if writing fails
     */
    void write(String text) throws ShellIOException;

    /**
     * Writing to user (console) given text.
     *
     * @param text to write to user
     * @throws ShellIOException if writing a line fails
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns unmodifiable map, so that the client can not delete commands by clearing the map.
     *
     * @return unmodifiable map
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Returns current MulitlineSymbol for this instance of Environment.
     *
     * @return current MulitlineSymbol for this instance of Environment
     */
    Character getMultilineSymbol();

    /**
     * Sets current MultilineSymbol for this instance of Environment.
     *
     * @param symbol of new MulitlineSymbol
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Returns current PromptSymbol for this instance of Environment.
     *
     * @return current PromptSymbol for this instance of Environment
     */
    Character getPromptSymbol();

    /**
     * Sets current PromptSymbol for this instance of Environment.
     *
     * @param symbol of new PromptSymbol
     */
    void setPromptSymbol(Character symbol);

    /**
     * Returns current MoreLineSymbol for this instance of Environment.
     *
     * @return current MoreLineSymbol for this instance of Environment
     */
    Character getMoreLinesSymbol();

    /**
     * Sets current MoreLinesSymbol for this instance of Environment.
     *
     * @param symbol of new MoreLinesSymbol
     */
    void setMoreLinesSymbol(Character symbol);

}
