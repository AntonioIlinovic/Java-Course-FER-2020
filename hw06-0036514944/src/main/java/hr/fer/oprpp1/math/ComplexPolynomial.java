package hr.fer.oprpp1.math;

import java.util.Arrays;

/**
 * Models polynomial over complex numbers with given template:                                                         .
 * f(z) = zn * z^n + z(n-1) * z^(n-1) + ... + z2 * z^2 + z1 * z + z0                                                   .
 * z0 to zn are coefficients which are attached to appropriate powers of z (and are given by user through constructor).
 * It is a polynomial of n-th order. All coefficients are given as complex numbers, and z is also an complex number.
 * Method apply accepts one concrete z and calculates the value of polynomial at that point.
 */
public class ComplexPolynomial {

    private final Complex[] factors;

    /**
     * Returns factors.
     *
     * @return factors
     */
    public Complex[] getFactors() {
        return factors;
    }

    /**
     * Returns factor with given order.
     *
     * @param order of returned factor
     * @return factor with given order
     */
    public Complex getFactor(int order) {
        return factors[order];
    }

    /**
     * Constructor for ComplexPolynomial which accepts coefficients (factors). Order of coefficients given in
     * constructor is from left to right: z0, z1, z2, ...
     *
     * @param factors coefficients which are attached to appropriate powers of z
     */
    public ComplexPolynomial(Complex... factors) {
        this.factors = factors;
    }

    /**
     * Returns order of this polynomial. For example order() for (7+2i)z^3+2z^2+5z+1 returns 3.
     *
     * @return order of this polynomial
     */
    public short order() {
        return (short) (factors.length - 1);
    }

    /**
     * Computes a new polynomial by multiplying this instance with given polynomial p.
     *
     * @param p to multiply this instance with
     * @return a new polynomial: this*p
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Complex[] multipliedFactors = new Complex[1 + this.order() + p.order()];
        Arrays.fill(multipliedFactors, Complex.ZERO);

        for (int thisOrder = 0; thisOrder <= this.order(); thisOrder++) {
            for (int pOrder = 0; pOrder <= p.order(); pOrder++) {
                int calculatedOrder = thisOrder + pOrder;

                multipliedFactors[calculatedOrder] = multipliedFactors[calculatedOrder].add(
                        this.getFactor(thisOrder).multiply(
                                p.getFactor(pOrder)));
            }
        }

        return new ComplexPolynomial(multipliedFactors);
    }

    /**
     * Computes first derivative of this polynomial. For example for (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
     *
     * @return first derivative of this polynomial
     */
    public ComplexPolynomial derive() {
        Complex[] derivedFactors = new Complex[order()];

        // First fill new array with all factors except that at index 0 (derivation will "eat" that one)
        for (int index = 0; index < derivedFactors.length; index++) {
            derivedFactors[index] = factors[index+1];
        }

        // Derive factors by multiplying by its old exponent
        for (int index = 1; index < derivedFactors.length; index++) {
            // TODO Possible optimization. There is no need to create new Complex object, just multiply real and imaginary value with multiplier
            derivedFactors[index] = derivedFactors[index].multiply(new Complex(index+1, 0));
        }

        return new ComplexPolynomial(derivedFactors);
    }

    /**
     * Computes polynomial value at given point z.
     *
     * @param z to calculate value at
     * @return polynomial value at point z
     */
    public Complex apply(Complex z) {
        // calculates at point z:  f(z) = zn * z^n + z(n-1) * z^(n-1) + ... + z2 * z^2 + z1 * z + z0
        Complex sum = Complex.ZERO;
        // zCurrentPower will be multiplied by z in each step -> 1, z, z^2, ... , z^n
        Complex zCurrentPower = Complex.ONE;

        for (Complex factor : factors) {
            sum = sum.add(factor.multiply(zCurrentPower));
            zCurrentPower = zCurrentPower.multiply(z);
        }

        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = factors.length - 1; i > 0; i--) {
            sb.append("(").append(factors[i]).append(")")
                    .append("*")
                    .append("z^").append(i).append("+");
        }
        sb.append("(").append(factors[0]).append(")");

        return sb.toString();
    }

}
