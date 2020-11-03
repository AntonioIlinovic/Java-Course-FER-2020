package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.collections.Dictionary;
import hr.fer.oprpp1.collections.ObjectStack;
import hr.fer.oprpp1.lsystems.impl.commands.Command;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;

import java.awt.*;

public class LSystemBuilderImpl implements LSystemBuilder {

    private Dictionary<Character, String> productions;
    private Dictionary<Character, Command> commands;
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
        this.angle = 0;
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

    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

    private class LSystemImpl implements LSystem {

        private ObjectStack<TurtleState> statesStack;


        @Override
        public String generate(int depth) {
            if (depth == 0)
                return axiom;

            return generate(depth - 1);
        }

        @Override
        public void draw(int depth, Painter painter) {
            this.statesStack = new ObjectStack<>();
            statesStack.push(new TurtleState(
                    origin,
                    new Vector2D(0, 0),
                    Color.BLACK,
                    unitLength
            ));

            generate(depth);
        }
    }
}
