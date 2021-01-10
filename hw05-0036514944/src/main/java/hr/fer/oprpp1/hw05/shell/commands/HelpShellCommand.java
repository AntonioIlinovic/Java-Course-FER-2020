package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.ShellUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Command help. If started with no arguments, it must list names of all supported commands. If started with single
 * argument, it must print name and the description of selected command (or print appropriate error message if no such
 * command exists).
 */
public class HelpShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            env.writeln("Supported commands:");
            env.commands().forEach((s, shellCommand) -> env.writeln(shellCommand.getCommandName()));
            return ShellStatus.CONTINUE;
        }

        String commandName = ShellUtil.extractFirstArgument(arguments, env);
        if (arguments.length() > commandName.length()) {
            env.writeln("Help command accepts only one argument!");
            return ShellStatus.CONTINUE;
        }

        ShellCommand command = env.commands().get(commandName);

        if (command == null) {
            env.writeln("Unsupported command!");
            return ShellStatus.CONTINUE;
        }

        env.writeln(command.getCommandName());
        command.getCommandDescription().forEach(env::writeln);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("If started with no arguments, it must list names of all supported commands.");
        commandDescription.add("If started with single argument, it must print name and the description of selected command.");
        commandDescription.add("Prints appropriate error message if no such command exists.");

        return commandDescription;
    }

}
