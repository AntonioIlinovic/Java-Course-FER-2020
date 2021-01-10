package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Command tree expects a single argument: directory name and prints a tree (each directory level shifts output two
 * characters to the right).
 */
public class TreeShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            env.writeln("Tree command accepts one argument!");
            return ShellStatus.CONTINUE;
        }

        String directoryName = ShellUtil.extractFirstArgument(arguments, env);
        if (directoryName.equals(""))
            return ShellStatus.CONTINUE;

        if (arguments.length() > ShellUtil.lengthOfFirstArgument(arguments)) {
            env.writeln("Tree command accepts only one argument!");
            return ShellStatus.CONTINUE;
        }

        Path start = Path.of(directoryName);
        if (start == null || !Files.exists(start)) {
            env.writeln("Directory doesn't exist!");
            return ShellStatus.CONTINUE;
        }
        try {
            Files.walkFileTree(start, new FileVisitor<Path>() {
                int indent = 0;

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    env.write(dir.getFileName().toString().indent(indent));
                    indent += 2;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    env.write(file.getFileName().toString().indent(indent));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    indent -= 2;
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException | NullPointerException e) {
            env.writeln("Check your Path!");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Expects a single argument: directory name.");
        commandDescription.add("Prints a tree (each directory level shifts output two characters to the right).");

        return commandDescription;
    }

}
