package hr.fer.oprpp1.hw05.shell;

/**
 * Class with utility methods for MyShell class.
 */
public class ShellUtil {

    /**
     * Returns command from a given line.
     *
     * @param line with command and arguments
     * @return command from a given line
     */
    public static String extractCommandFromLine(String line) {
        if (line.equals(""))
            return "";

        return line.split(" ", 2)[0];
    }

    /**
     * Returns arguments from a given line.
     *
     * @param line with command and arguments
     * @return arguments from a given line
     */
    public static String extractArgumentsFromLine(String line) {
        String[] split = line.split(" ", 2);

        return split.length == 1 ? "" : split[1].trim();
    }

    /**
     * Returns first argument from given argument. Method can accept quotes that have paths with spaces (such as
     * "Documents and Settings"). If line starts with quotation, \" is treated as escape sequence representing " as
     * regular character (and not string end). Additionally, a sequence \\ is treated as single  \. Every other
     * situation in which after \ follows anything but " and \ is copied as two characters, so that you can write
     * "C:\Documents and Settings\Users\javko". The described escaping mechanism is supported ONLY inside strings
     * started with double quote. After the ending double-quote, either no more characters must be present or at least
     * one space character must be present: strings like "C:\fi le".txt are invalid and argument parser will report an
     * error.
     *
     * @param line from which first argument is parsed
     * @return parser Path
     */
    public static String extractFirstArgument(String line, Environment env) {
        if (line.equals(""))
            return "";

        line = line.trim();
        int index = 0;

        if (line.charAt(index) == '"') {
            index++;
            StringBuilder sb = new StringBuilder("");
            boolean escaped = false;

            while (true) {
                if (index >= line.length()) {
                    env.writeln("Invalid Path with quotes!");
                    return "";
                }

                if (escaped) {
                    if (line.charAt(index) != '\\' && line.charAt(index) != '"') {
                        env.writeln("Invalid escaping in Path with quotes!");
                        return "";
                    }

                    escaped = false;
                    sb.append(line.charAt(index));
                } else {
                    if (line.charAt(index) == '\\')
                        escaped = true;
                    else if (line.charAt(index) == '"') {
                        index++;
                        // If after closing quote there is some character and it is not blank space
                        if (index < line.length() && line.charAt(index) != ' ') {
                            env.writeln("After close quote in Path there must be none, or blank space character!");
                            return "";
                        }
                        break;
                    } else
                        sb.append(line.charAt(index));
                }

                index++;
            }

            return sb.toString();
        }

        return line.split(" ")[0];
    }


    /*
     * check if user entered more than one argument: Do this by comparing lengths of full argument and parsed first
     * argument. Be sure to add to count not written escape characters in directoryName. Argument inside double quotes
     * '"' counts as a one argument.
     *
     * @param fullArgument        input after command name
     * @param parsedFirstArgument parsed first argument from fullArgument
     * @return if it has more than one argument
     */
    /*
    public static boolean hasMoreThanOneArgument(String fullArgument, String parsedFirstArgument) {
        if (fullArgument.startsWith("\"")) {
            int lengthWithEscapeChar = 2 + parsedFirstArgument.length();
            lengthWithEscapeChar += parsedFirstArgument.chars()
                    .filter(ch -> ch == '\\' || ch == '"')
                    .count();

            return fullArgument.length() > lengthWithEscapeChar;

        } else return fullArgument.length() > parsedFirstArgument.length();
    }
     */

    public static long numberOfCharsWithEscapeCounted(String pathName) {
        return pathName.chars()
                .filter(ch -> ch == '\\' || ch == '"')
                .count();
    }

    /**
     * Method counts the length of first parsed argument. First argument can be inside double quotes and all characters
     * including blank spaces are treated like one argument. Argument ends when there is closing double quotes that is
     * not escaped. If it doesn't start with quotes, argument ends when there is a blank space. It returns the length of
     * parsed argument.
     *
     * @param argument from which to parse first argument from
     * @return length of first parsed argument
     */
    public static int lengthOfFirstArgument(String argument) {
        argument = argument.trim();

        if (argument.equals(""))
            return 0;

        if (argument.startsWith("\"")) {
            boolean escaped = false;

            int index = 1;
            for (; index < argument.length(); index++) {
                if (escaped) {
                    escaped = false;
                    continue;
                }

                if (argument.charAt(index) == '"') {
                    index++;
                    break;
                }

                if (argument.charAt(index) == '\\') {
                    escaped = true;
                }
            }

            return index;
        }

        String[] split = argument.split(" ", 2);
        return split.length == 0 ? 0 : split[0].length();
    }

    public static String extractArgument(String argument) {
        return argument.split(" ")[0];
    }

    /**
     * Method that returns the whole command. If user entered multiline command, this method will write and read from
     * MyShell accordingly. MORELINES, MULTILINE and PROMPT symbol are written as the rules suggest. When user finishes
     * with the command, whole command is concatenated and returned to the user.
     *
     * @param env Shell environment
     * @return whole concatenated command
     */
    public static String getCommand(ShellEnvironment env) {
        String lastReadLine = env.readLine();
        String commandString = lastReadLine;
        // TODO Check if last symbol of each lastReadLine is by itself, for example this is wrong: symbol PROMPT #!

        while (lastCharEquals(commandString, env.getMoreLinesSymbol())) {
            commandString = commandString.substring(0, commandString.length() - 1);

            env.write(env.getMultilineSymbol() + " ");

            lastReadLine = env.readLine();
            commandString += lastReadLine;
        }

        return commandString;
    }

    /**
     * Private method used in getCommand() method. Checks if last char of given String is equal to the given char.
     *
     * @param s compare its last char
     * @param c char to compare with
     * @return if they are equal
     */
    private static boolean lastCharEquals(String s, Character c) {
        if (s == null || s.equals(""))
            return false;

        return s.charAt(s.length() - 1) == c;
    }
}
