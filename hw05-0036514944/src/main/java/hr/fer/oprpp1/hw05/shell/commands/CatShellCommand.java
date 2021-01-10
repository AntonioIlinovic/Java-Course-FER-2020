package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.ShellUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Command cat takes one or two arguments. The first argument is path to some file and is mandatory. The second argument
 * is charset name that should be used (see java.nio.charset.Charset class for details). This command opens given file
 * and writes its content to console.
 */
public class CatShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            env.writeln("Cat command accepts at least one argument!");
            return ShellStatus.CONTINUE;
        }

        String pathName = ShellUtil.extractFirstArgument(arguments, env);
        if (pathName.equals(""))
            return ShellStatus.CONTINUE;

        if (!Files.exists(Paths.get(pathName))) {
            env.writeln("File doesn't exist!");
            return ShellStatus.CONTINUE;
        }
        int firstArgumentEndIndex = ShellUtil.lengthOfFirstArgument(arguments);

        // TODO check indexes
        String argumentsWithoutFirst = arguments.substring(firstArgumentEndIndex).trim();
        String optionalCharsetName = ShellUtil.extractArgument(argumentsWithoutFirst);
        int secondArgumentEndIndex = ShellUtil.lengthOfFirstArgument(argumentsWithoutFirst);

        String argumentsWithoutSecond = argumentsWithoutFirst.substring(secondArgumentEndIndex).trim();
        if (!argumentsWithoutSecond.equals("")) {
            env.writeln("Cat command accepts at max two arguments!");
            return ShellStatus.CONTINUE;
        }

        Charset charset = Charset.defaultCharset();
        try {
            if (!optionalCharsetName.equals(""))
                charset = Charset.forName(optionalCharsetName);
        } catch (Exception e) {
            env.writeln("Invalid Charset name!");
            return ShellStatus.CONTINUE;
        }

        byte[] myBuffer = new byte[4096];
        int bytesRead = 0;
        try (InputStream in = Files.newInputStream(Path.of(pathName))) {
            while ((bytesRead = in.read(myBuffer, 0, 4096)) != -1) {
                CharBuffer cb = charset.decode(ByteBuffer.wrap(myBuffer, 0, bytesRead));
                env.write(cb.toString());
            }
            env.writeln("");
        } catch (FileNotFoundException e) {
            env.writeln("File not found!");
            return ShellStatus.CONTINUE;
        } catch (IOException e) {
            env.writeln("Error while reading file!");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Expects one or two arguments: first is path to some file and is mandatory.");
        commandDescription.add("Second argument is charset name that should be used to interpret chars from bytes.");
        commandDescription.add("If not provided, a default platform charset should be used.");
        commandDescription.add("This command opens given file and writes its content to console.");

        return commandDescription;
    }

}
