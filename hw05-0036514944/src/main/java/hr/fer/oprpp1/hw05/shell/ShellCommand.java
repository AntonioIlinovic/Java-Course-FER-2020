package hr.fer.oprpp1.hw05.shell;

import java.util.List;

/**
 * Interface for all specific shell commands.
 */
public interface ShellCommand {

    /**
     * Method which executes ShellCommand using the state of given Environment and arguments. Second argument is a
     * single String which represents everything that user entered AFTER the command name. It is expected that in case
     * of multiline input, the shell has already concatenated all lines into a single line and removed MORELINES symbol
     * from line endings (before concatenation). This way, the command will always get a single line with arguments.
     *
     * @param env       Environment through which current ShellCommand is executed
     * @param arguments arguments for ShellCommand that user has entered
     * @return ShellStatus after the command is executed
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Method returns the name of the command.
     *
     * @return the name of the command
     */
    String getCommandName();

    /**
     * Method returns a description (usage instructions). Since the description can span more than one line, a read-only
     * List must be used.
     *
     * @return command usage instructions
     */
    List<String> getCommandDescription();

}
