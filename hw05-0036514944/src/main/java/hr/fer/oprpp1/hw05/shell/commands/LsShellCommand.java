package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * Command ls takes a single argument (directory) and writes a directory listing (not recursive). Output should be
 * formatted as in following example.
 *
 * <p>
 * -rw- 53412 2009-03-15 12:59:31 azuriraj.ZIP                                                                         .
 * drwx 4096 2011-06-08 12:59:31 b                                                                                     .
 * drwx 4096 2011-09-19 12:59:31 backup                                                                                .
 * -rw- 17345597 2009-02-18 12:59:31 backup-ferko-20090218.tgz                                                         .
 * drwx 4096 2008-11-09 12:59:31 beskonacno                                                                            .
 * drwx 4096 2010-10-29 12:59:31 bin                                                                                   .
 * -rwx 282 2011-02-10 12:59:31 burza.sh                                                                               .
 * -rwx 281 2011-02-10 12:59:31 burza.sh~                                                                              .
 * -rwx 1316 2009-09-10 12:59:31 burza_stat.sh                                                                         .
 * drwx 4096 2011-09-02 12:59:31 ca                                                                                    .
 * drwx 4096 2008-09-02 12:59:31 CA                                                                                    .
 * -rw- 0 2008-09-02 12:59:31 ca.key
 * </p>
 * The output consists of 4 columns. First column indicates if current object is directory (d), readable (r), writable
 * (w) and executable (x). Second column contains object size in bytes that is right aligned and occupies 10 characters.
 * Follows file creation date/time and finally file name.
 */
public class LsShellCommand implements ShellCommand {

    /*
    To obtain file attributes (such as creation date/time), see the following snippet.

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Path path = Paths.get("d:/tmp/javaPrimjeri/readme.txt");
    BasicFileAttributeView faView = Files.getFileAttributeView(
        path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
    );
    BasicFileAttributes attributes = faView.readAttributes();
    FileTime fileTime = attributes.creationTime();
    String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
    System.out.println(formattedDateTime);
     */

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            env.writeln("Ls command accepts at least one argument!");
            return ShellStatus.CONTINUE;
        }

        String dirPathName = ShellUtil.extractFirstArgument(arguments, env);
        if (dirPathName.equals(""))
            return ShellStatus.CONTINUE;

        if (arguments.length() > ShellUtil.lengthOfFirstArgument(arguments)) {
            env.writeln("Ls command accepts only one argument!");
            return ShellStatus.CONTINUE;
        }

        Path dirPath = Paths.get(dirPathName);
        if (!Files.isDirectory(dirPath)) {
            env.writeln("Given path is not directory!");
            return ShellStatus.CONTINUE;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (Stream<Path> paths = Files.walk(dirPath, 1)) {
            paths.filter(path -> !path.equals(dirPath))
                    .forEach(path -> {

                        BasicFileAttributeView faView = Files.getFileAttributeView(
                                path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
                        );
                        BasicFileAttributes attributes = null;
                        try {
                            attributes = faView.readAttributes();
                        } catch (IOException e) {
                            env.writeln("Error in reading File attributes!");
                            return;
                        }
                        FileTime fileTime = attributes.creationTime();
                        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

                        boolean isDirectory = attributes.isDirectory();
                        boolean isReadable = Files.isReadable(path);
                        boolean isWritable = Files.isWritable(path);
                        boolean isExecutable = Files.isExecutable(path);

                        String sb = (isDirectory ? "d" : "-") +
                                (isReadable ? "r" : "-") +
                                (isWritable ? "w" : "-") +
                                (isExecutable ? "x" : "-") +
                                " " +
                                "%10.10s".formatted(attributes.size()) +
                                " " +
                                formattedDateTime +
                                " " +
                                path.getFileName();

                        env.writeln(sb);

                    });
        } catch (IOException e) {
            env.writeln("Error while walking the directory!");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Takes a single argument: directory.");
        commandDescription.add("Writes a directory listing (not recursive).");
        commandDescription.add("Output is formatted as in following example:");
        commandDescription.add("");
        commandDescription.add("-rw- 53412 2009-03-15 12:59:31 azuriraj.ZIP");
        commandDescription.add("drwx 4096 2011-06-08 12:59:31 b");
        commandDescription.add("drwx 4096 2011-09-19 12:59:31 backup ");
        commandDescription.add("-rw- 17345597 2009-02-18 12:59:31 backup-ferko-20090218.tgz");
        commandDescription.add("drwx 4096 2008-11-09 12:59:31 beskonacno");
        commandDescription.add("");
        commandDescription.add("The output consists of 4 columns:");
        commandDescription.add("first column indicates if current object is directory (d), readable (r), writable (w) and executable (x).");
        commandDescription.add("Second column contains object size in bytes that is right aligned and occupies 10 characters.");
        commandDescription.add("Third column contains file creation date/time.");
        commandDescription.add("Fourth column contains file name.");

        return commandDescription;
    }

}
