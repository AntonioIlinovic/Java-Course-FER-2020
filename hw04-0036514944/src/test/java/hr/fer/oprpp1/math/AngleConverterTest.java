package hr.fer.oprpp1.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AngleConverterTest {

    @Test
    public void testRadiansToDegreesMethod() {
        assertEquals(57.2958, AngleConverter.RadiansToDegrees(1.0), 0.001);
        assertEquals(-859.437, AngleConverter.RadiansToDegrees(-15.0), 0.001);
        assertEquals(5793.17627, AngleConverter.RadiansToDegrees(101.11), 0.001);
        assertEquals(5420.181, AngleConverter.RadiansToDegrees(94.6), 0.001);
        assertEquals(0.0, AngleConverter.RadiansToDegrees(0.0), 0.001);
    }

    @Test
    public void testDegreesToRadiansMethod() {
        assertEquals(2.62933852, AngleConverter.DegreesToRadians(150.65), 0.001);
        assertEquals(-1.8404497, AngleConverter.DegreesToRadians(-105.45), 0.001);
        assertEquals(14.7110312, AngleConverter.DegreesToRadians(842.88), 0.001);
        assertEquals(-78.7527465, AngleConverter.DegreesToRadians(-4512.2), 0.001);
        assertEquals(0.0, AngleConverter.DegreesToRadians(0.0), 0.001);
    }

    @Test
    public void testVector2DRotation() {
        Vector2D i = new Vector2D(1.0, 0.0);
        Vector2D j = i.rotated(AngleConverter.DegreesToRadians(90));
    }
}
