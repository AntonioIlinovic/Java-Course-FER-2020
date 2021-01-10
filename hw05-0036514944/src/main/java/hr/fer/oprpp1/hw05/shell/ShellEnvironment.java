package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.util.*;

/**
 * Implementation of a Environment for MyShell Environment. It stores the current state of MyShell (current Prompt,
 * MultiLine, MoreLines symbols, available commands, user input and output).
 */
public class ShellEnvironment implements Environment {

    private Character multiLineSymbol;
    private Character promptSymbol;
    private Character moreLinesSymbol;

    private final Scanner scanner;

    private final SortedMap<String, ShellCommand> commands;

    /**
     * Empty constructor for new ShellEnvironment. It initializes default symbols and map of commands.
     */
    public ShellEnvironment() {
        multiLineSymbol = '|';
        promptSymbol = '>';
        moreLinesSymbol = '\\';

        scanner = new Scanner(System.in);

        commands = new TreeMap<>();
        commands.put("exit", new ExitShellCommand());
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("cat", new CatShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("tree", new TreeShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("help", new HelpShellCommand());
        commands.put("symbol", new SymbolShellCommand());
    }

    @Override
    public String readLine() throws ShellIOException {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        try {
            System.out.print(text);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        try {
            System.out.println(text);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(commands);
    }

    @Override
    public Character getMultilineSymbol() {
        return multiLineSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        multiLineSymbol = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        promptSymbol = symbol;
    }

    @Override
    public Character getMoreLinesSymbol() {
        return moreLinesSymbol;
    }

    @Override
    public void setMoreLinesSymbol(Character symbol) {
        moreLinesSymbol = symbol;
    }

}
