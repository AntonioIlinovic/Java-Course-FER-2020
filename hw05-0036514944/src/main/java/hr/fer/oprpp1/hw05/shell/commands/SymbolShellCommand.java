package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Command symbol that can be used to change PROMPTSYMBOL, MORELINESSYMBOL and MULTILINESYMBOL. See example:
 * <p>
 * Welcome to MyShell v 1.0                                                                                            .
 * > symbol PROMPT                                                                                                     .
 * Symbol for PROMPT is '>'                                                                                            .
 * > symbol PROMPT #                                                                                                   .
 * Symbol for PROMPT changed from '>' to '#'                                                                           .
 * # symbol \                                                                                                          .
 * | MORELINES \                                                                                                       .
 * | !                                                                                                                 .
 * Symbol for MORELINES changed from '\' to '!'                                                                        .
 * # symbol !                                                                                                          .
 * | MORELINES                                                                                                         .
 * Symbol for MORELINES is '!'                                                                                         .
 * # symbol MULTILINE                                                                                                  .
 * Symbol for MULTILINE is '|'                                                                                         .
 * # exit
 * </p>
 */
public class SymbolShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] argumentsSplit = arguments.split(" ");
        if (argumentsSplit.length <= 0 || argumentsSplit.length > 2) {
            env.writeln("Symbol command accepts one or two arguments!");
            return ShellStatus.CONTINUE;
        }

        String symbolType = argumentsSplit[0];
        if (!symbolType.equals("PROMPT")
                && !symbolType.equals("MORELINES")
                && !symbolType.equals("MULTILINE")) {
            env.writeln("Invalid symbol type!");
            return ShellStatus.CONTINUE;
        }

        Character environmentOldSymbol = ' ';
        switch (symbolType) {
            case "PROMPT" -> environmentOldSymbol = env.getPromptSymbol();
            case "MORELINES" -> environmentOldSymbol = env.getMoreLinesSymbol();
            case "MULTILINE" -> environmentOldSymbol = env.getMultilineSymbol();
        }

        if (argumentsSplit.length == 1) {
            env.writeln("Symbol for " + symbolType + " is '" + environmentOldSymbol + "'");
        } else {
            if (argumentsSplit[1].length() > 1) {
                env.writeln("New symbol must be of length 1!");
                return ShellStatus.CONTINUE;
            }

            Character environmentNewSymbol = argumentsSplit[1].charAt(0);
            switch (symbolType) {
                case "PROMPT" -> env.setPromptSymbol(environmentNewSymbol);
                case "MORELINES" -> env.setMoreLinesSymbol(environmentNewSymbol);
                case "MULTILINE" -> env.setMultilineSymbol(environmentNewSymbol);
            }

            env.writeln("Symbol for " + symbolType + " changed from '" + environmentOldSymbol + "' to '" + environmentNewSymbol + "'");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Used to change PROMPT, MORELINES or MULTILINE symbols.");
        commandDescription.add("If you enter just one argument (one of three SYMBOLS), it prints current SYMBOL.");
        commandDescription.add("If you enter a second argument (a character), it will update old SYMBOL to a newly given one.");

        return commandDescription;
    }

}