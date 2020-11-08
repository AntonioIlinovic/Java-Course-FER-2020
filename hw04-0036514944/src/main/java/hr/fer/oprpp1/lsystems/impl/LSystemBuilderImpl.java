package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.collections.Dictionary;
import hr.fer.oprpp1.collections.ObjectStack;
import hr.fer.oprpp1.collections.Processor;
import hr.fer.oprpp1.lsystems.impl.commands.*;
import hr.fer.oprpp1.lsystems.impl.parser.LSystemParser;
import hr.fer.oprpp1.math.AngleConverter;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;

import java.awt.*;
import java.util.Objects;

/**
 * Class implements LSystemBuilder. It has two Dictionaries (one for registered productions, and other for registered
 * commands). Method build() returns one concrete implementation of LSystem.
 */
public class LSystemBuilderImpl implements LSystemBuilder {

    /* Supported commands */
    private final String COMMAND_DRAW = "draw";
    private final String COMMAND_SKIP = "skip";
    private final String COMMAND_SCALE = "scale";
    private final String COMMAND_ROTATE = "rotate";
    private final String COMMAND_PUSH = "push";
    private final String COMMAND_POP = "pop";
    private final String COMMAND_COLOR = "color";

    /* Dictionary for registered productions */
    private Dictionary<Character, String> productions;
    /* Dictionary for registered commands */
    private Dictionary<Character, Command> commands;

    /* How long is unit length turtle move. (e.g. value of 0.5 means that one unitLength move is equal to the half of window size */
    private double unitLength;
    /* Used so that dimensions of drawn fractal stay more or less the same. If it is generated for depth d, unitLength
    must be scaled appropriately. (start effective unitLength must be set to: [unitLength * (unitLengthDegreeScaler^d)] ) */
    private double unitLengthDegreeScaler;
    /* Point from where turtle starts */
    private Vector2D origin;
    /* Angle in which turtle is turned relative to positive x-axis */
    private double angle;
    /* Sequence from which the system starts, it can be one or multiple symbols */
    private String axiom;

    /**
     * Constructor which sets default configuration of the system.
     */
    public LSystemBuilderImpl() {
        this.productions = new Dictionary<>();
        this.commands = new Dictionary<>();
        this.unitLength = 0.1;
        this.unitLengthDegreeScaler = 1;
        this.origin = new Vector2D(0, 0);
        this.angle = 0.0;
        this.axiom = "";
    }

    /**
     * Configures unitLength.
     *
     * @param unitLength to set
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder setUnitLength(double unitLength) {
        this.unitLength = unitLength;
        return this;
    }

    /**
     * Configures origin.
     *
     * @param x to set
     * @param y to set
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder setOrigin(double x, double y) {
        this.origin = new Vector2D(x, y);
        return this;
    }

    /**
     * Configures angle.
     *
     * @param angle to set
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    /**
     * Configures axiom.
     *
     * @param axiom to set
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder setAxiom(String axiom) {
        this.axiom = axiom;
        return this;
    }

    /**
     * Configures unitLengthDegreeScaler.
     *
     * @param unitLengthDegreeScaler to set
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
        this.unitLengthDegreeScaler = unitLengthDegreeScaler;
        return this;
    }

    /**
     * Registers new production using given arguments.
     *
     * @param productionKey   key of new production
     * @param productionValue value of new production
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder registerProduction(char productionKey, String productionValue) {
        productions.put(productionKey, productionValue);
        return this;
    }

    /**
     * Registers new command using given arguments.
     *
     * @param commandKey   key of new command
     * @param commandLine value of new command
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder registerCommand(char commandKey, String commandLine) {
        Command newCommand = generateCommand(commandLine);
        commands.put(commandKey, newCommand);
        return this;
    }

    /**
     * Configures this object using given arguments.
     *
     * @param strings configuration values
     * @return this LSystemBuilder
     */
    @Override
    public LSystemBuilder configureFromText(String[] strings) {
        LSystemParser parserAndConfigurer = new LSystemParser(this, strings);
        return parserAndConfigurer.parseAndConfigure();
    }

    /**
     * Helper method which gets a String from which it generates a new Command and returns it.
     *
     * @param commandLine String which helps it to generate a Command ("draw 1", "rotate 60", "push", "G color
     *                    00ff00"...)
     * @return a new generated Command
     */
    private Command generateCommand(String commandLine) {
        if (commandLine == null)
            return null;

        String[] commandLineTokens = commandLine.split("\\s+");
        String commandName = commandLineTokens[0].toLowerCase();

        /* This switch is just used to check if command line is written in expected format. If you register
          commands that are valid, this is not needed. */
        switch (commandName) {
            case COMMAND_PUSH:
            case COMMAND_POP:
                if (commandLineTokens.length != 1)
                    throw new RuntimeException("Invalid command line: " + commandLine);
                break;
            case COMMAND_COLOR:
            case COMMAND_DRAW:
            case COMMAND_ROTATE:
            case COMMAND_SKIP:
            case COMMAND_SCALE:
                if (commandLineTokens.length != 2)
                    throw new RuntimeException("Invalid command line: " + commandLine);
        }

        return switch (commandName) {
            case COMMAND_PUSH -> new PushCommand();
            case COMMAND_POP -> new PopCommand();
            case COMMAND_COLOR -> new ColorCommand((HexToRGB(commandLineTokens[1])));
            case COMMAND_DRAW -> new DrawCommand(Double.parseDouble(commandLineTokens[1]));
            case COMMAND_ROTATE -> new RotateCommand(Double.parseDouble(commandLineTokens[1]));
            case COMMAND_SKIP -> new SkipCommand(Double.parseDouble(commandLineTokens[1]));
            case COMMAND_SCALE -> new ScaleCommand(Double.parseDouble(commandLineTokens[1]));
            default -> throw new RuntimeException("Unsupported command line: " + commandLine);
        };
    }

    /**
     * Helper method which accepts String as a hex value e.g. "00FF33" and returns new Color object using given
     * argument.
     *
     * @param hexString String as a hex value e.g. "00FF33"
     * @return new Color depending on argument hexString
     */
    private Color HexToRGB(String hexString) {
        return new Color(
                Integer.valueOf(hexString.substring(0, 2), 16),
                Integer.valueOf(hexString.substring(2, 4), 16),
                Integer.valueOf(hexString.substring(4, 6), 16)
        );
    }

    /**
     * Helper method to generate one depth of productions on given String.
     *
     * @param currentProduction to apply productions on
     * @return generated production on given String
     */
    private String replaceWithProductions(String currentProduction) {
        StringBuilder newProduction = new StringBuilder();
        currentProduction.chars()
                .mapToObj(c -> (char) c)
                .forEach(c -> {
                    if (productions.get(c) != null)
                        newProduction.append(productions.get(c));
                    else
                        newProduction.append(c);
                });
        return newProduction.toString();
    }

    /**
     * Method returns one concrete Lindermayers system by given configuration. It can be configured by calling configure
     * methods or configureFromText method.
     *
     * @return one concrete, configured Lindermayrs LSystem
     */
    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

    /**
     * Interface LSystem models one Lindenmayers system.
     */
    private class LSystemImpl implements LSystem {

        private Context context;
        private String production = axiom;

        /**
         * Method accepts depth and returns a generated sequence after depth applications of productions. If depth is 0,
         * axiom is returned, for depth 1 sequence that is generated by application of production on axiom is returned.
         * Generally if the depth is k, sequence that is generated by applying k-1 productions is returned. This method
         * doesn't draw anything. It accepts one argument depth and returns a String which represents sequence of
         * symbols that was generated at that depth.
         *
         * @param depth number of applications of productions
         * @return sequence generated by productions
         */
        @Override
        public String generate(int depth) {
            if (depth == 0) {
                String temp = production;
                production = axiom;
                return temp;
            }

            production = replaceWithProductions(production);

            return generate(depth - 1);
        }

        /**
         * Uses given Painter to draw final fractal. It uses method generate to get a sequence of symbols for given
         * depth, and using Painter object draws symbol by symbol.
         *
         * @param depth   number of applications of productions
         * @param painter draws final fractal using this object
         */
        @Override
        public void draw(int depth, Painter painter) {
            context = new Context();
            context.pushState(new TurtleState(
                    origin,
                    new Vector2D(1.0, 0.0).rotated(AngleConverter.DegreesToRadians(angle)),
                    Color.BLACK,
                    unitLength * Math.pow(unitLengthDegreeScaler, depth) // TODO Check this
            ));

            production = axiom;
            production = generate(depth);

            production.chars()
                    .mapToObj(commandKey -> commands.get((char)commandKey))
                    .filter(Objects::nonNull)
                    .forEach(command -> command.execute(context, painter));
        }
    }
}
