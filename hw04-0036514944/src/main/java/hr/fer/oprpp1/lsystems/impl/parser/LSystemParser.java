package hr.fer.oprpp1.lsystems.impl.parser;

import hr.fer.zemris.lsystems.LSystemBuilder;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Parser implementation for LSystem configuring. It will parse and configure given LSystemBuilder from given commandLines.
 */
public class LSystemParser {

    /* Instance of LSystemBuilder to configure */
    private LSystemBuilder lSystemBuilder;
    /* Command lines to parse */
    private String[] commandLines;

    /**
     * Constructor for LSystemParser. It accepts an instance of LSystemBuilder that needs configuration and commandLines
     * that are used for configuration.
     * @param lSystemBuilder an instance of LSystemBuilder that needs configuration
     * @param commandLines that are used for configuration
     */
    public LSystemParser(LSystemBuilder lSystemBuilder, String[] commandLines) {
        this.lSystemBuilder = lSystemBuilder;
        this.commandLines = commandLines;
    }

    /**
     * Method which parses and configures this instance of LSystemBuilder.
     * @return configured instance of LSystemBuilder
     */
    public LSystemBuilder parseAndConfigure() {
        for (String commandLine : commandLines) {
            if (commandLine.isEmpty())
                continue;

            String[] commandLineTokens = commandLine.split("\\s+");
            String commandType = commandLineTokens[0];
            switch (commandType.toLowerCase()) {
                case "origin" -> configureOrigin(commandLineTokens);
                case "angle" -> configureAngle(commandLineTokens);
                case "unitlength" -> configureUnitLength(commandLineTokens);
                case "unitlengthdegreescaler" -> configureUnitLengthDegreeScaler(commandLineTokens);
                case "command" -> configureCommand(commandLineTokens);
                case "axiom" -> configureAxiom(commandLineTokens);
                case "production" -> configureProduction(commandLineTokens);
                default -> throw new RuntimeException("Invalid command line");
            }
        }

        lSystemBuilder.build();
        return lSystemBuilder;
    }

    /**
     * Adds new production to LSystemBuilder.
     * @param commandLineTokens used to configure
     */
    private void configureProduction(String[] commandLineTokens) {
        if (commandLineTokens.length != 3)
            throw new RuntimeException("Invalid command line for command: " + commandLineTokens[0]);

        if (commandLineTokens[1].length() != 1)
            throw new RuntimeException("Production key must be of length 1");
        char productionKey = commandLineTokens[1].charAt(0);
        String productionValue = String.join(" ", Arrays.copyOfRange(commandLineTokens, 2, commandLineTokens.length));

        lSystemBuilder.registerProduction(productionKey, productionValue);
    }

    /**
     * Adds new axiom to LSystemBuilder.
     * @param commandLineTokens used to configure
     */
    private void configureAxiom(String[] commandLineTokens) {
        if (commandLineTokens.length != 2)
            throw new RuntimeException("Invalid command line for command: " + commandLineTokens[0]);

        lSystemBuilder.setAxiom(commandLineTokens[1]);
    }

    /**
     * Adds new command to LSystemBuilder.
     * @param commandLineTokens used to configure
     */
    private void configureCommand(String[] commandLineTokens) {
        if (commandLineTokens.length < 3)
            throw new RuntimeException("Invalid command line for command: " + commandLineTokens[0]);

        if (commandLineTokens[1].length() != 1)
            throw new RuntimeException("Command key must be of length 1");

        char commandKey = commandLineTokens[1].charAt(0);
        String commandValue = String.join(" ", Arrays.copyOfRange(commandLineTokens, 2, commandLineTokens.length));

        lSystemBuilder.registerCommand(commandKey, commandValue);
    }

    /**
     * Adds new unitLengthDegreeScaler to LSystemBuilder.
     * @param commandLineTokens used to configure
     */
    private void configureUnitLengthDegreeScaler(String[] commandLineTokens) {
        if (commandLineTokens.length < 2)
            throw new RuntimeException("Invalid command line for command: " + commandLineTokens[0]);

        double unitLengthDegreeScaler;

        /* UnitLengthDegreeScaler can be in formats: ("1.0", "1.0 /3.0", "1.0 / 3.0", "1.0/3.0") */
        String[] numberTokens = Arrays.copyOfRange(commandLineTokens, 1, commandLineTokens.length);
        String token = String.join("", numberTokens);

        if (!token.contains("/")) {
            try {
                unitLengthDegreeScaler = Double.parseDouble(token);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Cannot convert " + token + " to double");
            }
        } else {
            int indexOfSplit = token.indexOf("/");
            try {
                double left = Double.parseDouble(token.substring(0, indexOfSplit));
                double right = Double.parseDouble(token.substring(indexOfSplit+1));
                unitLengthDegreeScaler = left / right;
            } catch (NumberFormatException e) {
                throw new RuntimeException("Cannot convert " + token + " to double");
            }
        }

        lSystemBuilder.setUnitLengthDegreeScaler(unitLengthDegreeScaler);
    }

    /**
     * Adds new unitLength to LSystemBuilder.
     * @param commandLineTokens used to configure
     */
    private void configureUnitLength(String[] commandLineTokens) {
        if (commandLineTokens.length != 2)
            throw new RuntimeException("Invalid command line for command: " + commandLineTokens[0]);

        double unitLength;
        try {
            unitLength = Double.parseDouble(commandLineTokens[1]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert " + commandLineTokens[1] + " to double");
        }

        lSystemBuilder.setUnitLength(unitLength);
    }

    /**
     * Adds new angle to LSystemBuilder.
     * @param commandLineTokens used to configure
     */
    private void configureAngle(String[] commandLineTokens) {
        if (commandLineTokens.length != 2)
            throw new RuntimeException("Invalid command line for command: " + commandLineTokens[0]);

        double angle;
        try {
            angle = Double.parseDouble(commandLineTokens[1]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert " + commandLineTokens[1] + " to double");
        }

        lSystemBuilder.setAngle(angle);
    }

    /**
     * Adds new origin to LSystemBuilder.
     * @param commandLineTokens used to configure
     */
    private void configureOrigin(String[] commandLineTokens) {
        if (commandLineTokens.length != 3)
            throw new RuntimeException("Invalid command line for command: " + commandLineTokens[0]);

        double x;
        double y;
        try {
            x = Double.parseDouble(commandLineTokens[1]);
            y = Double.parseDouble(commandLineTokens[2]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert " + commandLineTokens[1] + " " + commandLineTokens[2] + " to double");
        }

        lSystemBuilder.setOrigin(x, y);
    }

}
