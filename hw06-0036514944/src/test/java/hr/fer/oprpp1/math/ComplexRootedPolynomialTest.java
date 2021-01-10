package hr.fer.oprpp1.math;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexRootedPolynomialTest {

    @Test
    public void testApply() {
        Complex c = new ComplexRootedPolynomial(
                new Complex(5, 2), new Complex(3, 4), new Complex(1, 1))
                .apply(new Complex(3, 3));

        assertEquals(new Complex(14, -6), c);
    }

    @Test
    public void testToComplexPolynom() {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        ComplexPolynomial cp = crp.toComplexPolynom();
        assertEquals("(2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0)", cp.toString());

        // TODO check
        /*
        ComplexPolynomial polynomial = new ComplexRootedPolynomial(
                new Complex(5, 2), new Complex(3, 4), new Complex(1, 1))
                .toComplexPolynom();

        Complex[] factors = polynomial.getFactors();

        assertEquals(new Complex(19, -33), factors[0]);
        assertEquals(new Complex(9, 40), factors[1]);
        assertEquals(new Complex(-9, -7), factors[2]);
        assertEquals(new Complex(1, 0), factors[3]);
         */
    }

    @Test
    public void testIndexOfClosestRootFor() {
        int closestRoot = new ComplexRootedPolynomial(
                new Complex(5, 2), new Complex(3, 4), new Complex(1, 1))
                .indexOfClosestRootFor(new Complex(3.0000001, 4), 1E-3);

        assertEquals(0, closestRoot);
    }

    @Test
    public void testIndexOfClosestRootForNoClosest() {
        int closestRoot = new ComplexRootedPolynomial(
                new Complex(5, 2), new Complex(3, 4), new Complex(1, 1))
                .indexOfClosestRootFor(new Complex(20.0000001, 4), 1E-3);

        assertEquals(-1, closestRoot);
    }

    @Test
    public void testToString() {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        assertEquals("(2.0+i0.0)*(z-(1.0+i0.0))*(z-(-1.0+i0.0))*(z-(0.0+i1.0))*(z-(0.0-i1.0))",
                crp.toString());
    }

}
