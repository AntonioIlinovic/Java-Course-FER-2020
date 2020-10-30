package hr.fer.oprpp1.hw01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexNumberTest {

    /**
     * Check if two ComplexNumber are equal with epsilon allowed difference.
     *
     * @param c1 first complex number.
     * @param c2 second complex number.
     * @param epsilon allowed difference between numbers.
     * @return true if equal, false otherwise.
     */
    private static boolean ComplexNumbersEqual(ComplexNumber c1, ComplexNumber c2, double epsilon) {
        return Math.abs(c1.getReal() - c2.getReal()) < epsilon
                && Math.abs(c1.getImaginary() - c2.getImaginary()) < epsilon;
    }

    @Test
    public void testFromRealMethod() {
        ComplexNumber result = ComplexNumber.fromReal(5.5);
        assertEquals(5.5, result.getReal());
        assertEquals(0.0, result.getImaginary());
    }

    @Test
    public void testFromImaginaryMethod() {
        ComplexNumber result = ComplexNumber.fromImaginary(5.5);
        assertEquals(0.0, result.getReal());
        assertEquals(5.5, result.getImaginary());
    }

    @Test
    public void testFromMagnitudeAndAngleMethod() {
        ComplexNumber result1 = ComplexNumber.fromMagnitudeAndAngle(48.21086,0.96853);
        assertEquals(27.31191, result1.getReal(), 0.0001);
        assertEquals(39.72841, result1.getImaginary(), 0.001);

        ComplexNumber result2 = ComplexNumber.fromMagnitudeAndAngle(91.55033, 4.27705);
        assertEquals(-38.60809, result2.getReal(), 0.001);
        assertEquals(-83.01131, result2.getImaginary(), 0.001);
    }

    @Test
    public void testParseMethodWithIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-+2.7"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-2.5+-3.15i"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("i33"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-i2.4"));
    }

    @Test
    public void testParseMethodWithValidArguments() {
        ComplexNumber result1 = ComplexNumber.parse("351");
        assertEquals(351.0, result1.getReal());
        assertEquals(0.0, result1.getImaginary());

        ComplexNumber result2 = ComplexNumber.parse("-3.17");
        assertEquals(-3.17, result2.getReal());
        assertEquals(0.0, result2.getImaginary());

        ComplexNumber result3 = ComplexNumber.parse("i");
        assertEquals(0.0, result3.getReal());
        assertEquals(1.0, result3.getImaginary());

        ComplexNumber result4 = ComplexNumber.parse("-i");
        assertEquals(0.0, result4.getReal());
        assertEquals(-1.0, result4.getImaginary());

        ComplexNumber result5 = ComplexNumber.parse("-2.71-3.15i");
        assertEquals(-2.71, result5.getReal());
        assertEquals(-3.15, result5.getImaginary());

        ComplexNumber result6 = ComplexNumber.parse("-1-i");
        assertEquals(-1.0, result6.getReal());
        assertEquals(-1.0, result6.getImaginary());

        ComplexNumber result7 = ComplexNumber.parse("+2.71+3.15i");
        assertEquals(2.71, result7.getReal());
        assertEquals(3.15, result7.getImaginary());
    }

    @Test
    public void testGetRealMethod() {
        ComplexNumber result = new ComplexNumber(4.2, 5.3);
        assertEquals(4.2, result.getReal());
    }

    @Test
    public void testGetImaginaryMethod() {
        ComplexNumber result = new ComplexNumber(4.2, 5.3);
        assertEquals(5.3, result.getImaginary());
    }

    @Test
    public void testGetMagnitudeMethod() {
        ComplexNumber result1 = new ComplexNumber(4.444,5.555);
        assertEquals(7.11387, result1.getMagnitude(), 0.001);

        ComplexNumber result2 = new ComplexNumber(-5, -4);
        assertEquals(6.40312, result2.getMagnitude(), 0.001);
    }

    @Test
    public void testGetAngle() {
        ComplexNumber result1 = new ComplexNumber(4.444,5.555);
        assertEquals(0.89606, result1.getAngle(), 0.001);

        ComplexNumber result2 = new ComplexNumber(-5, -4);
        assertEquals(3.81633, result2.getAngle(), 0.001);

        ComplexNumber result3 = new ComplexNumber(-1, 1);
        assertEquals(2.35619, result3.getAngle(), 0.001);

        ComplexNumber result4 = new ComplexNumber(1, -1);
        assertEquals(5.49779, result4.getAngle(), 0.001);
    }

    @Test
    public void testAddMethod() {
        ComplexNumber add1 = new ComplexNumber(1.5, -2.0);
        ComplexNumber add2 = new ComplexNumber(2.0, 3.5);
        ComplexNumber result = add1.add(add2);
        assertEquals(3.5, result.getReal());
        assertEquals(1.5, result.getImaginary());
    }

    @Test
    public void testSubMethod() {
        ComplexNumber sub1 = new ComplexNumber(1.5, -2.0);
        ComplexNumber sub2 = new ComplexNumber(2.0, 3.5);
        ComplexNumber result = sub1.sub(sub2);
        assertEquals(-0.5, result.getReal());
        assertEquals(-5.5, result.getImaginary());
    }

    @Test
    public void testMulMethod() {
        ComplexNumber mul1 = new ComplexNumber(1.5, -2.0);
        ComplexNumber mul2 = new ComplexNumber(2.0, 3.5);
        ComplexNumber result = mul1.mul(mul2);
        assertEquals(10, result.getReal(), 0.001);
        assertEquals(1.25, result.getImaginary(), 0.001);

        mul1 = new ComplexNumber(3.7,-5.2);
        mul2 = new ComplexNumber(-1.3, -2.5);
        result = mul1.mul(mul2);
        assertEquals(-17.81, result.getReal(), 0.001);
        assertEquals(-2.49, result.getImaginary(), 0.001);
    }

    @Test
    public void testDivMethod() {
        ComplexNumber div1 = new ComplexNumber(3.7, -5.2);
        ComplexNumber div2 = new ComplexNumber(-1.3, -2.5);
        ComplexNumber result = div1.div(div2);
        assertEquals(1.03148, result.getReal(), 0.001);
        assertEquals(2.01637, result.getImaginary(), 0.001);

        div1 = new ComplexNumber(1.6, -6.3);
        div2 = new ComplexNumber(-6.3, 2.4);
        result = div1.div(div2);
        assertEquals(-0.55445, result.getReal(), 0.001);
        assertEquals(0.78877, result.getImaginary(), 0.001);
    }

    @Test
    public void testPowerMethod() {
        ComplexNumber c1 = new ComplexNumber(5.3, -6.2);
        ComplexNumber result1 = c1.power(6);
        assertEquals(132999.90844, result1.getReal(), 0.001);
        assertEquals(262732.18914, result1.getImaginary(), 0.001);

        ComplexNumber c2 = new ComplexNumber(-7.2, 5.5);
        ComplexNumber result2 = c2.power(5);
        assertEquals(60616.09368, result2.getReal(), 0.001);
        assertEquals(-7312.85225, result2.getImaginary(), 0.001);
    }

    @Test
    public void testRootMethod() {
        ComplexNumber c1 = new ComplexNumber(-7.2, 5.5);
        ComplexNumber[] rootsC1 = c1.root(3);
        ComplexNumber[] expectedRootsC1 = new ComplexNumber[] {
                new ComplexNumber(1.4073, 1.5380),
                new ComplexNumber(-2.0356, 0.44975),
                new ComplexNumber(0.62832, -1.9878)
        };
        assertTrue(ComplexNumbersEqual(rootsC1[0], expectedRootsC1[0], 0.001));
        assertTrue(ComplexNumbersEqual(rootsC1[1], expectedRootsC1[1], 0.001));
        assertTrue(ComplexNumbersEqual(rootsC1[2], expectedRootsC1[2], 0.001));

    }

    @Test
    public void testToStringMethod() {
        ComplexNumber c1 = new ComplexNumber(2.5, -11.0);
        assertEquals("2.5-11.0i", c1.toString());

        ComplexNumber c2 = new ComplexNumber(-3, 4);
        assertEquals("-3.0+4.0i", c2.toString());

        ComplexNumber c3 = new ComplexNumber(1.0, 0.0);
        assertEquals("1.0", c3.toString());
    }

}
