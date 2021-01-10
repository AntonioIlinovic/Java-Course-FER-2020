package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Command charsets takes no arguments and lists names of supported charsets for your Java platform (see
 * Charset.availableCharsets()). A single charset name is written per line.
 */
public class CharsetsShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.length() != 0) {
            env.writeln("charsets command takes no arguments!");
            return ShellStatus.CONTINUE;
        }

        Charset.availableCharsets().values()
                .forEach(c -> {
                    env.writeln(c.displayName());
                });

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Takes no arguments and lists names of supported charsets for your Java platform.");
        commandDescription.add("A single charset name is written per line.");

        return commandDescription;
    }

}
