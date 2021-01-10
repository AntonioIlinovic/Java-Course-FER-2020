package hr.fer.oprpp1.math;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexTest {

    /**
     * Check if two Complex are equal with epsilon allowed difference.
     *
     * @param c1      first complex number.
     * @param c2      second complex number.
     * @param epsilon allowed difference between numbers.
     * @return true if equal, false otherwise.
     */

    private static boolean ComplexNumbersEqual(Complex c1, Complex c2, double epsilon) {
        return Math.abs(c1.getReal() - c2.getReal()) < epsilon
                && Math.abs(c1.getImaginary() - c2.getImaginary()) < epsilon;
    }

    @Test
    public void testParseMethodWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> Complex.parse("-+2.7"));
        assertThrows(IllegalArgumentException.class, () -> Complex.parse("-2.5+-3.15i"));
        //assertThrows(IllegalArgumentException.class, () -> Complex.parse("i33"));
        //assertThrows(IllegalArgumentException.class, () -> Complex.parse("-i2.4"));
    }

    @Test
    public void testParseMethodWithValidArguments() {
        Complex result1 = Complex.parse("351");
        assertEquals(351.0, result1.getReal());
        assertEquals(0.0, result1.getImaginary());

        Complex result2 = Complex.parse("-3.17");
        assertEquals(-3.17, result2.getReal());
        assertEquals(0.0, result2.getImaginary());

        Complex result3 = Complex.parse("i");
        assertEquals(0.0, result3.getReal());
        assertEquals(1.0, result3.getImaginary());

        Complex result4 = Complex.parse("-i");
        assertEquals(0.0, result4.getReal());
        assertEquals(-1.0, result4.getImaginary());

        Complex result5 = Complex.parse("-2.71-i3.15");
        assertEquals(-2.71, result5.getReal());
        assertEquals(-3.15, result5.getImaginary());

        Complex result6 = Complex.parse("-1-i");
        assertEquals(-1.0, result6.getReal());
        assertEquals(-1.0, result6.getImaginary());

        Complex result7 = Complex.parse("+2.71+i3.15");
        assertEquals(2.71, result7.getReal());
        assertEquals(3.15, result7.getImaginary());
    }

    @Test
    public void testGetRealMethod() {
        Complex result = new Complex(4.2, 5.3);
        assertEquals(4.2, result.getReal());
    }

    @Test
    public void testGetImaginaryMethod() {
        Complex result = new Complex(4.2, 5.3);
        assertEquals(5.3, result.getImaginary());
    }

    @Test
    public void testModuleMethod() {
        Complex result1 = new Complex(4.444, 5.555);
        assertEquals(7.11387, result1.module(), 0.001);

        Complex result2 = new Complex(-5, -4);
        assertEquals(6.40312, result2.module(), 0.001);
    }

    @Test
    public void testAddMethod() {
        Complex add1 = new Complex(1.5, -2.0);
        Complex add2 = new Complex(2.0, 3.5);
        Complex result = add1.add(add2);
        assertEquals(3.5, result.getReal());
        assertEquals(1.5, result.getImaginary());
    }

    @Test
    public void testSubMethod() {
        Complex sub1 = new Complex(1.5, -2.0);
        Complex sub2 = new Complex(2.0, 3.5);
        Complex result = sub1.sub(sub2);
        assertEquals(-0.5, result.getReal());
        assertEquals(-5.5, result.getImaginary());
    }

    @Test
    public void testMultiplyMethod() {
        Complex mul1 = new Complex(1.5, -2.0);
        Complex mul2 = new Complex(2.0, 3.5);
        Complex result = mul1.multiply(mul2);
        assertEquals(10, result.getReal(), 0.001);
        assertEquals(1.25, result.getImaginary(), 0.001);

        mul1 = new Complex(3.7, -5.2);
        mul2 = new Complex(-1.3, -2.5);
        result = mul1.multiply(mul2);
        assertEquals(-17.81, result.getReal(), 0.001);
        assertEquals(-2.49, result.getImaginary(), 0.001);
    }

    @Test
    public void testDivideMethod() {
        Complex div1 = new Complex(3.7, -5.2);
        Complex div2 = new Complex(-1.3, -2.5);
        Complex result = div1.divide(div2);
        assertEquals(1.03148, result.getReal(), 0.001);
        assertEquals(2.01637, result.getImaginary(), 0.001);

        div1 = new Complex(1.6, -6.3);
        div2 = new Complex(-6.3, 2.4);
        result = div1.divide(div2);
        assertEquals(-0.55445, result.getReal(), 0.001);
        assertEquals(0.78877, result.getImaginary(), 0.001);
    }

    @Test
    public void testPowerMethod() {
        Complex c1 = new Complex(5.3, -6.2);
        Complex result1 = c1.power(6);
        assertEquals(132999.90844, result1.getReal(), 0.001);
        assertEquals(262732.18914, result1.getImaginary(), 0.001);

        Complex c2 = new Complex(-7.2, 5.5);
        Complex result2 = c2.power(5);
        assertEquals(60616.09368, result2.getReal(), 0.001);
        assertEquals(-7312.85225, result2.getImaginary(), 0.001);
    }

    @Test
    public void testRootMethod() {
        Complex c1 = new Complex(-7.2, 5.5);
        List<Complex> rootsC1 = c1.root(3);
        List<Complex> expectedRootsC1 = new ArrayList<>();
        expectedRootsC1.add(new Complex(1.4073, 1.5380));
        expectedRootsC1.add(new Complex(-2.0356, 0.44975));
        expectedRootsC1.add(new Complex(0.62832, -1.9878));

        assertTrue(ComplexNumbersEqual(rootsC1.get(0), expectedRootsC1.get(0), 0.001));
        assertTrue(ComplexNumbersEqual(rootsC1.get(1), expectedRootsC1.get(1), 0.001));
        assertTrue(ComplexNumbersEqual(rootsC1.get(2), expectedRootsC1.get(2), 0.001));

    }

    @Test
    public void testToStringMethod() {
        Complex c1 = new Complex(2.5, -11.0);
        assertEquals("2.5-i11.0", c1.toString());

        Complex c2 = new Complex(-3, 4);
        assertEquals("-3.0+i4.0", c2.toString());

        Complex c3 = new Complex(1.0, 0.0);
        assertEquals("1.0+i0.0", c3.toString());
    }

}
