package hr.fer.oprpp1.hw05.shell;


import java.nio.file.InvalidPathException;
import java.util.Arrays;

import static hr.fer.oprpp1.hw05.shell.ShellUtil.extractArgumentsFromLine;
import static hr.fer.oprpp1.hw05.shell.ShellUtil.extractCommandFromLine;

/**
 * When started, program MyShell writes a greeting message to user. Writes a prompt symbol and waits for the user to
 * enter a command. The command can span across multiple lines. However, each line that is not the last line of command
 * must end with a special symbol that is used to inform the shell that more lines are expected. We will refer to these
 * symbols as PROMPTSYMBOL and MORELINESSYMBOL. For each line that is part of multi-line command (except for the first
 * one) a shell must write MULTILINESYMBOL ath the beginning followed by a single whitespace. Shell must provide a
 * command symbol that can be used to change these symbols.
 */
public class MyShell {

    public static void main(String[] args) {
        ShellEnvironment env = new ShellEnvironment();
        ShellStatus shellStatus = ShellStatus.CONTINUE;
        env.writeln("Welcome to MyShell v 1.0");
        do {
            env.write(env.getPromptSymbol() + " ");

            String line = ShellUtil.getCommand(env);
            String commandName = extractCommandFromLine(line);
            String arguments = extractArgumentsFromLine(line);

            ShellCommand command = env.commands().get(commandName);
            if (command == null)
                env.writeln("Invalid command!");
            else {
                try {
                    shellStatus = command.executeCommand(env, arguments);
                } catch (InvalidPathException e) {
                    env.writeln("Invalid Path!");
                }
            }

        } while (shellStatus != ShellStatus.TERMINATE);

    }

}
