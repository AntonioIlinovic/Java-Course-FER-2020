package hr.fer.oprpp1.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Vector2DTest {

    private static double vector1_x = 10.5;
    private static double vector1_y = -3.5;
    private static double vector2_x = -5.0;
    private static double vector2_y = -5.0;


    private static Vector2D getVector_1() {
        return new Vector2D(vector1_x, vector1_y);
    }

    private static Vector2D getVector_2() {
        return new Vector2D(vector2_x, vector2_y);
    }

    @Test
    public void testAddMethod() {
        Vector2D vect1 = getVector_1();
        Vector2D vect2 = getVector_2();
        vect1.add(vect2);
        assertEquals(vector1_x + vector2_x, vect1.getX());
        assertEquals(vector1_y + vector2_y, vect1.getY());
    }

    @Test
    public void testAddedMethod() {
        Vector2D vect1 = getVector_1();
        Vector2D vect2 = getVector_2();
        Vector2D result = vect1.added(vect2);
        assertEquals(vector1_x + vector2_x, result.getX());
        assertEquals(vector1_y + vector2_y, result.getY());
    }

    @Test
    public void testRotateMethod() {
        Vector2D vect1 = getVector_1();
        vect1.rotate(Math.PI);
        assertEquals(-vector1_x, vect1.getX(), 0.001);
        assertEquals(-vector1_y, vect1.getY(), 0.001);

        Vector2D vect2 = getVector_2();
        vect2.rotate(Math.PI / 2);
        assertEquals(-vector2_x, vect2.getX(), 0.001);
        assertEquals(vector2_y, vect2.getY(), 0.001);
    }

    @Test
    public void testRotatedMethod() {
        Vector2D vect1 = getVector_1();
        Vector2D result = vect1.rotated(Math.PI);
        assertEquals(-vector1_x, result.getX(), 0.001);
        assertEquals(-vector1_y, result.getY(), 0.001);

        Vector2D vect2 = getVector_2();
        result = vect2.rotated(Math.PI / 2);
        assertEquals(-vector2_x, result.getX(), 0.001);
        assertEquals(vector2_y, result.getY(), 0.001);
    }

    @Test
    public void testScaleMethod() {
        Vector2D vect1 = getVector_1();
        vect1.scale(2.5);
        assertEquals(vector1_x * 2.5, vect1.getX(), 0.001);
        assertEquals(vector1_y * 2.5, vect1.getY(), 0.001);

        Vector2D vect2 = getVector_2();
        vect2.scale(-5.2);
        assertEquals(vector2_x * (-5.2), vect2.getX(), 0.001);
        assertEquals(vector2_y * (-5.2), vect2.getY(), 0.001);
    }

    @Test
    public void testScaledMethod() {
        Vector2D vect1 = getVector_1();
        Vector2D result = vect1.scaled(2.5);
        assertEquals(vector1_x * 2.5, result.getX(), 0.001);
        assertEquals(vector1_y * 2.5, result.getY(), 0.001);

        Vector2D vect2 = getVector_2();
        result = vect2.scaled(-5.2);
        assertEquals(vector2_x * (-5.2), result.getX(), 0.001);
        assertEquals(vector2_y * (-5.2), result.getY(), 0.001);
    }

    @Test
    public void testCopyMethod() {
        Vector2D vect1 = getVector_1();
        Vector2D copied = vect1.copy();
        assertEquals(vect1.getX(), copied.getX());
        assertEquals(vect1.getY(), copied.getY());
    }
}
