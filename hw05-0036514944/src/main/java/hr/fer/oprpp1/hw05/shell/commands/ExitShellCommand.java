package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Command exit takes no arguments and exits MyShell.
 */
public class ExitShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.length() != 0) {
            env.writeln("exit command takes no arguments!");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("It doesn't accept any arguments.");
        commandDescription.add("Exits MyShell.");

        return commandDescription;
    }

}
