package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.ShellUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Command mkdir takes a single argument: directory name, and creates the appropriate directory structure.
 */
public class MkdirShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            env.writeln("Mkdir command accepts one argument!");
            return ShellStatus.CONTINUE;
        }

        String directoryName = ShellUtil.extractFirstArgument(arguments, env);
        if (directoryName.equals(""))
            return ShellStatus.CONTINUE;

        if (arguments.length() > ShellUtil.lengthOfFirstArgument(arguments)) {
            env.writeln("Mkdir command accepts only one argument!");
            return ShellStatus.CONTINUE;
        }

        File f = new File(directoryName);

        if (f.mkdir())
            env.writeln("Directory '" + directoryName + "' is created.");
        else
            env.writeln("Unable to create directory '" + directoryName + "'.");

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Takes a single argument: directory name.");
        commandDescription.add("Creates the appropriate directory structure.");

        return commandDescription;
    }

}
