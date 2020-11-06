package hr.fer.oprpp1.lsystems.demo;

import hr.fer.oprpp1.lsystems.impl.LSystemBuilderImpl;
import hr.fer.oprpp1.math.AngleConverter;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;

public class Glavni1 {
    public static void main(String[] args) {
        LSystemViewer.showLSystem(createPlant1(LSystemBuilderImpl::new));
    }

    private static LSystem createKochCurve(LSystemBuilderProvider provider) {
        return provider.createLSystemBuilder()
                .registerCommand('F', "draw 1")
                .registerCommand('+', "rotate 60")
                .registerCommand('-', "rotate -60")
                .setOrigin(0.05, 0.4)
                .setAngle(0)
                .setUnitLength(0.9)
                .setUnitLengthDegreeScaler(1.0/3.0)
                .registerProduction('F', "F+F--F+F")
                .setAxiom("F")
                .build();
    }

    private static LSystem createKochCurve2(LSystemBuilderProvider provider) {
        String[] data = new String[] {
                "origin 0.05 0.4",
                "angle 0",
                "unitLength 0.9",
                "unitLengthDegreeScaler 1.0/3.0",
                "",
                "command F draw 1",
                "command + rotate 60",
                "command - rotate -60",
                "",
                "axiom F",
                "",
                "production F F+F--F+F"
        };
        return provider.createLSystemBuilder().configureFromText(data).build();
    }

    private static LSystem createKochCurve3(LSystemBuilderProvider provider) {
        return provider.createLSystemBuilder()
                .registerCommand('F', "draw 1")
                .registerCommand('+', "rotate 90")
                .registerCommand('-', "rotate -90")
                .setOrigin(0.3, 0.75)
                .setAngle(0)
                .setUnitLength(0.45)
                .setUnitLengthDegreeScaler(1.0/4.0)
                .registerProduction('F', "F-F+F+FF-F-F+F")
                .setAxiom("F-F-F-F")
                .build();
    }

    private static LSystem createPlant1(LSystemBuilderProvider provider) {
        return provider.createLSystemBuilder()
                .setOrigin(0.5, 0.0)
                .setAngle(90)
                .setUnitLength(0.1)
                .setUnitLengthDegreeScaler(1.0/2.05)
                .registerCommand('F', "draw 1")
                .registerCommand('+', "rotate 25.7")
                .registerCommand('-', "rotate -25.7")
                .registerCommand('[', "push")
                .registerCommand(']', "pop")
                .registerCommand('G', "color 00FF00")
                .setAxiom("GF")
                .registerProduction('F', "F[+F]F[-F]F")
                .build();
    }

    private static LSystem createPlant2(LSystemBuilderProvider provider) {
        return provider.createLSystemBuilder()
                .setOrigin(0.5, 0.0)
                .setAngle(90)
                .setUnitLength(0.3)
                .setUnitLengthDegreeScaler(1.0/2.05)
                .registerCommand('F', "draw 1")
                .registerCommand('+', "rotate 25")
                .registerCommand('-', "rotate -25")
                .registerCommand('[', "push")
                .registerCommand(']', "pop")
                .registerCommand('G', "color 00FF00")
                .setAxiom("GF")
                .registerProduction('F', "FF+[+F-F-F]-[-F+F+F]")
                .build();
    }

    private static LSystem createPlant3(LSystemBuilderProvider provider) {
        return provider.createLSystemBuilder()
                .setOrigin(0.5, 0.0)
                .setAngle(90)
                .setUnitLength(0.5)
                .setUnitLengthDegreeScaler(1.0/2.05)
                .registerCommand('F', "draw 1")
                .registerCommand('+', "rotate 20")
                .registerCommand('-', "rotate -20")
                .registerCommand('[', "push")
                .registerCommand(']', "pop")
                .registerCommand('G', "color 00FF00")
                .setAxiom("GB")
                .registerProduction('B', "F[+B]F[-B]+B")
                .registerProduction('F', "FF")
                .build();
    }
}
