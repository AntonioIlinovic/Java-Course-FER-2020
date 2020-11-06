package hr.fer.oprpp1.lsystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LSystemBuilderImplTest {

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

    @Test
    public void testGenerateMethod() {
        LSystem lSystem = createKochCurve(LSystemBuilderImpl::new);
        String depth_0 = lSystem.generate(0);
        String depth_1 = lSystem.generate(1);
        String depth_2 = lSystem.generate(2);
        assertEquals("F", depth_0);
        assertEquals("F+F--F+F", depth_1);
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", depth_2);
    }

}
