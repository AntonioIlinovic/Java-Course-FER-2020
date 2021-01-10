package hr.fer.oprpp1.math;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexPolynomialTest {

    @Test
    public void testOrder() {
        ComplexPolynomial polynomial = new ComplexPolynomial(
                new Complex(1, 0), new Complex(5, 0),
                new Complex(2, 0), new Complex(7, 2));

        assertEquals(3, polynomial.order());
    }

    @Test
    public void testMultiply() {
        ComplexPolynomial polynomial = new ComplexPolynomial(
                new Complex(1, 0), new Complex(5, 0),
                new Complex(2, 0),new Complex(7, 2))
                .multiply(new ComplexPolynomial(new Complex(2, 2), new Complex(1, 1)));

        Complex[] factors = polynomial.getFactors();

        assertEquals(new Complex(2, 2), factors[0]);
        assertEquals(new Complex(11, 11), factors[1]);
        assertEquals(new Complex(9, 9), factors[2]);
        assertEquals(new Complex(12, 20), factors[3]);
        assertEquals(new Complex(5, 9), factors[4]);
    }

    @Test
    public void testDerive() {
        ComplexPolynomial polynomial = new ComplexPolynomial(
                new Complex(1, 0), new Complex(5, 0),
                new Complex(2, 0), new Complex(7, 2))
                .derive();

        Complex[] factors = polynomial.getFactors();

        assertEquals(new Complex(5, 0), factors[0]);
        assertEquals(new Complex(4, 0), factors[1]);
        assertEquals(new Complex(21, 6), factors[2]);


        ComplexPolynomial cp = new ComplexPolynomial(
                new Complex(-2,0), Complex.ZERO, Complex.ZERO, Complex.ZERO, new Complex(2,0)
        );
        assertEquals("(8.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(0.0+i0.0)", cp.derive().toString());
    }

    @Test
    public void testApply() {
        Complex c = new ComplexPolynomial(
                new Complex(1, 0), new Complex(5, 0),
                new Complex(2, 0), new Complex(7, 2))
                .apply(new Complex(1, 1));

        assertEquals(new Complex(-12, 19), c);
    }

    @Test
    public void testGetFactors() {
        ComplexPolynomial polynomial = new ComplexPolynomial(
                new Complex(1, 0), new Complex(5, 0),
                new Complex(2, 0), new Complex(7, 2));

        Complex[] factors = polynomial.getFactors();

        assertEquals(new Complex(1, 0), factors[0]);
        assertEquals(new Complex(5, 0), factors[1]);
        assertEquals(new Complex(2, 0), factors[2]);
        assertEquals(new Complex(7, 2), factors[3]);

    }

    @Test
    public void testToString() {
        ComplexPolynomial cp = new ComplexPolynomial(
                new Complex(-2, 0), Complex.ZERO, Complex.ZERO, Complex.ZERO, new Complex(2, 0)
        );
        assertEquals("(2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0)",
                cp.toString());
    }

}
