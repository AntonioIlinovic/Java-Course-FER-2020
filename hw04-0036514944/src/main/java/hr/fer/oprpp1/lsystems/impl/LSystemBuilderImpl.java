package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.collections.Dictionary;
import hr.fer.oprpp1.collections.ObjectStack;
import hr.fer.oprpp1.collections.Processor;
import hr.fer.oprpp1.lsystems.impl.commands.*;
import hr.fer.oprpp1.math.AngleConverter;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;

import java.awt.*;

public class LSystemBuilderImpl implements LSystemBuilder {

    private Dictionary<Character, String> productions;
    private Dictionary<Character, String> commands;
    private double unitLength;
    private double unitLengthDegreeScaler;
    private Vector2D origin;
    private double angle;
    private String axiom;

    public LSystemBuilderImpl() {
        this.productions = new Dictionary<>();
        this.commands = new Dictionary<>();
        this.unitLength = 0.1;
        this.unitLengthDegreeScaler = 1;
        this.origin = new Vector2D(0, 0);
        this.angle = 0.0;
        this.axiom = "";
    }

    @Override
    public LSystemBuilder setUnitLength(double unitLength) {
        this.unitLength = unitLength;
        return this;
    }

    @Override
    public LSystemBuilder setOrigin(double x, double y) {
        this.origin = new Vector2D(x, y);
        return this;
    }

    @Override
    public LSystemBuilder setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    @Override
    public LSystemBuilder setAxiom(String axiom) {
        this.axiom = axiom;
        return this;
    }

    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
        this.unitLengthDegreeScaler = unitLengthDegreeScaler;
        return this;
    }

    @Override
    public LSystemBuilder registerProduction(char productionKey, String productionValue) {
        productions.put(productionKey, productionValue);
        return this;
    }

    @Override
    public LSystemBuilder registerCommand(char commandKey, String commandValue) {
        commands.put(commandKey, commandValue);
        return this;
    }

    @Override
    public LSystemBuilder configureFromText(String[] strings) {
        // TODO
        return null;
    }

    private ArrayIndexedCollection<Command> commandsFromProduction(String production) {
        ArrayIndexedCollection<Command> commandsToExecute = new ArrayIndexedCollection<>();

        production.chars()
                .mapToObj(c -> (char) c)
                .forEach(c -> {
                    String commandLine = commands.get(c);
                    commandsToExecute.add(generateCommand(commandLine));
                });

        return commandsToExecute;
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
            return new DrawCommand(0.0); // TODO This is here just because method must return something, fix it

        String[] commandLineTokens = commandLine.split(" ");
        String commandName = commandLineTokens[0].toLowerCase();

        return switch (commandName) {
            case "color" -> new ColorCommand((HexToRGB(commandLineTokens[1])));
            case "draw" -> new DrawCommand(Double.parseDouble(commandLineTokens[1]));
            case "rotate" -> new RotateCommand(Double.parseDouble(commandLineTokens[1]));
            case "skip" -> new SkipCommand(Double.parseDouble(commandLineTokens[1]));
            case "push" -> new PushCommand();
            case "pop" -> new PopCommand();
            case "scale" -> new ScaleCommand(Double.parseDouble(commandLineTokens[1]));
            default -> new DrawCommand(0.0); // TODO This is here just because method must return something, fix it
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

    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

    private class LSystemImpl implements LSystem {

        private Context context;
        private String production = axiom;

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

            ArrayIndexedCollection<Command> commandToExecute = commandsFromProduction(production);
            commandToExecute.forEach(value -> value.execute(context, painter));
        }
    }

    class LSystemLexer {

    }
}
