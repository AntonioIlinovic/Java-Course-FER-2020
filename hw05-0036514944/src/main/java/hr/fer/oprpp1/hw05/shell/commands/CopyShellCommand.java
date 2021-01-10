package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Command copy expects two arguments: source file and destination file name (i.e. paths and names). If destination
 * file exists, you should ask user is it allowed to overwrite it. Your copy command must work only with files (no
 * directories). If the second argument is directory, you should assume that user wants to copy the original file into
 * that directory using the original file name. You must implement copying yourself: you are not allowed to simply call
 * copy methods from Files class.
 */
public class CopyShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            env.writeln("Copy command accepts two arguments!");
            return ShellStatus.CONTINUE;
        }

        String sourcePathName = ShellUtil.extractFirstArgument(arguments, env);
        if (sourcePathName.equals(""))
            return ShellStatus.CONTINUE;

        int firstArgumentLength = ShellUtil.lengthOfFirstArgument(arguments);
        if (arguments.length() == firstArgumentLength) {
            env.writeln("Copy accepts two arguments!");
            return ShellStatus.CONTINUE;
        }

        String argumentsWithoutFirst = arguments.substring(firstArgumentLength).trim();

        String destinationPathName = ShellUtil.extractFirstArgument(argumentsWithoutFirst, env);
        if (destinationPathName.equals(""))
            return ShellStatus.CONTINUE;

        int secondArgumentLength = ShellUtil.lengthOfFirstArgument(argumentsWithoutFirst);
        String argumentsWithoutSecond = argumentsWithoutFirst.substring(secondArgumentLength);
        if (!argumentsWithoutSecond.equals("")) {
            env.writeln("Copy accepts two arguments!");
            return ShellStatus.CONTINUE;
        }

        Path sourcePath = Paths.get(sourcePathName);
        Path destinationPath = Paths.get(destinationPathName);

        if (Files.isDirectory(sourcePath)) {
            env.writeln("Source must be file, not directory!");
            return ShellStatus.CONTINUE;
        }

        if (Files.isDirectory(destinationPath)) {
            try (InputStream in = new BufferedInputStream(new FileInputStream(sourcePathName));
                 OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationPathName + "\\" +  sourcePath.getFileName().toString()))) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                    out.flush();
                }

            } catch (FileNotFoundException e) {
                env.writeln("File not found!");
                return ShellStatus.CONTINUE;
            } catch (IOException e) {
                throw new ShellIOException(e.getMessage());
            }
        } else {
            if (Files.exists(destinationPath)) {
                env.write("Destination path exists. Overwrite it?\ny - if acceptable\n" + env.getPromptSymbol() + " ");
                boolean overwrite = env.readLine().equalsIgnoreCase("y");
                if (!overwrite) {
                    env.writeln("Exiting copy command!");
                    return ShellStatus.CONTINUE;
                }
            }

            try (InputStream in = new BufferedInputStream(new FileInputStream(sourcePathName));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationPathName))) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                    out.flush();
                }

            } catch (FileNotFoundException e) {
                env.writeln("File not found!");
                return ShellStatus.CONTINUE;
            } catch (IOException e) {
                throw new ShellIOException(e.getMessage());
            }
        }

        env.writeln(sourcePathName + " copied to " + destinationPathName);
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Expects two arguments: source file name and destination file name (i.e. paths and names).");
        commandDescription.add("If destination file exists, you should ask user is it allowed to overwrite it.");
        commandDescription.add("Command works only with files (no directories).");
        commandDescription.add("If the second argument is directory, it is assumed that user wants to copy  the original file into that directory using the original file name.");

        return commandDescription;
    }

}
