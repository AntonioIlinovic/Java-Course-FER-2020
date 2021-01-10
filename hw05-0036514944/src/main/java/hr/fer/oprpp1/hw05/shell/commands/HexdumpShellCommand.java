package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Command hexdump expects a single argument: file name, and produces hex-output as illustrated below. On the right side
 * of the image only a standard subset of characters is shown; for all other characters a '.' is printed instead (i.e.
 * replace all  bytes whose value is less than 32 or greater than 127 with '.').
 * <p>
 * 00000000: 31 2E 20 4F 62 6A 65 63|74 53 74 61 63 6B 20 69 | 1. ObjectStack i                                        .
 * 00000010: 6D 70 6C 65 6D 65 6E 74|61 63 69 6A 61 0D 0A 32 | mplementacija..2                                        .
 * 00000020: 2E 20 4D 6F 64 65 6C 2D|4C 69 7. 74 65 6E 65 72 | . Model-Listener                                        .
 * 00000030: 20 69 6D 70 6C 65 6D 65|6E 74 61 63 69 6A 61 0D |  implementacija.                                        .
 * 00000040: 0A                     |                        | .
 * </p>
 */
public class HexdumpShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            env.writeln("Hexdump command accepts one argument!");
            return ShellStatus.CONTINUE;
        }

        String pathFileName = ShellUtil.extractFirstArgument(arguments, env);

        if (arguments.length() > ShellUtil.lengthOfFirstArgument(arguments)) {
            env.writeln("Hexdump command accepts only one argument!");
            return ShellStatus.CONTINUE;
        }

        Path file = Paths.get(pathFileName);
        if (Files.isDirectory(file)) {
            env.writeln("Hexdump command accepts file, not directory!");
            return ShellStatus.CONTINUE;
        }

        int lineCounter = 0;

        try (InputStream in = new BufferedInputStream(new FileInputStream(pathFileName))) {

            byte[] buffer = new byte[16];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {

                StringBuilder sb = new StringBuilder(String.format("%08X", lineCounter)).append(":");
                lineCounter += bytesRead;

                int byteIndex = 0;
                for (; byteIndex < 8; byteIndex++) {
                    sb.append(" ");

                    if (byteIndex < bytesRead) {
                        sb.append(String.format("%02X", buffer[byteIndex]));

                        if (buffer[byteIndex] < 32 || buffer[byteIndex] > 127)
                            buffer[byteIndex] = 46;
                    } else {
                        sb.append("  ");
                    }
                }

                sb.append("|");

                for (; byteIndex < 16; byteIndex++) {
                    if (byteIndex < bytesRead) {
                        sb.append(String.format("%02X", buffer[byteIndex]));

                        if (buffer[byteIndex] < 32 || buffer[byteIndex] > 127)
                            buffer[byteIndex] = 46;
                    } else {
                        sb.append("  ");
                    }

                    sb.append(" ");
                }

                sb.append("| ");
                for (byteIndex = 0; byteIndex < 16 && byteIndex < bytesRead; byteIndex++)
                    sb.append(Character.toString(buffer[byteIndex]));

                env.writeln(sb.toString());
            }

        } catch (FileNotFoundException e) {
            env.writeln("File not found!");
            return ShellStatus.CONTINUE;
        } catch (IOException e) {
            throw new ShellIOException(e.getMessage());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        List<String> commandDescription = new ArrayList<>();

        commandDescription.add("Expects a single argument: file name.");
        commandDescription.add("Produces hex-output as illustrated below.");
        commandDescription.add("On the right side of the image only a standard subset of characters is shown.");
        commandDescription.add("For all other characters a '.' is printed instead (i.e. replace all bytes whose value is less than 32 or greater than 127 with '.').");
        commandDescription.add("");
        commandDescription.add("00000000: 31 2E 20 4F 62 6A 65 63|74 53 74 61 63 6B 20 69 | 1. ObjectStack i");
        commandDescription.add("00000010: 6D 70 6C 65 6D 65 6E 74|61 63 69 6A 61 0D 0A 32 | mplementacija..2");
        commandDescription.add("00000020: 2E 20 4D 6F 64 65 6C 2D|4C 69 7. 74 65 6E 65 72 | . Model-Listener");
        commandDescription.add("00000030: 20 69 6D 70 6C 65 6D 65|6E 74 61 63 69 6A 61 0D |  implementacija.");
        commandDescription.add("00000040: 0A                     |                        | .");

        return commandDescription;

    }

}
