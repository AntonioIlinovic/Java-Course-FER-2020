package hr.fer.oprpp1.math;

/**
 * Models polynomial over complex numbers with given template:                                                         .
 * f(z) = z0 * (z - z1) * (z - z2) * ... * (z - z(n-1)) * (z - zn)                                                     .
 * z1 to zn are roots of a function, and z0 is a constant (z0 to zn are given by user through constructor). It is a
 * polynomial of n-th order. All zi are given as complex numbers, and z is also an complex number. Method apply accepts
 * one concrete z and calculates the value of polynomial in that point.
 */
public class ComplexRootedPolynomial {

    private final Complex constant;
    // TODO Check if you can store roots in List<Complex>
    private final Complex[] roots;

    /**
     * Constructor for ComplexRootedPolynomial. constant and roots are all Complex numbers.
     *
     * @param constant z0 - constant complex number
     * @param roots    z1 to zn - roots of a function
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        this.constant = constant;
        this.roots = roots;
    }

    /**
     * Computes polynomial value at given point z.
     *
     * @param z to calculate value at
     * @return polynomial value at point z
     */
    public Complex apply(Complex z) {
        // calculates at point z:  f(z) = z0 * (z - z1) * (z - z2) * ... * (z - z(n-1)) * (z - zn)
        Complex temp = constant;

        for (Complex root : roots) {
            temp = temp.multiply(z.sub(root));
        }

        return temp;
    }

    /**
     * Converts this representation to ComplexPolynomial type.
     *
     * @return this instance represented as ComplexPolynomial
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial temp = new ComplexPolynomial(constant);

        for (Complex root : roots) {
            temp = temp.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
        }

        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(").append(constant).append(")");

        for (Complex root : roots) {
            sb.append("*(z-(").append(root).append("))");
        }

        return sb.toString();
    }

    /**
     * Finds index of closest root for given complex number z that is within threshold. If there is no such root,
     * returns -1. First root has index 0, second index 1, etc.
     *
     * @param z         finds closest root from this Complex
     * @param threshold range to find closest root within
     * @return index of closest root for given complex number z that is within threshold, or -1 it there is no such root
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        int minIndex = -1;
        double minDistance = threshold;

        for (int index = 0; index < roots.length; index++) {
            double difference = z.sub(roots[index]).module();

            if (difference < minDistance) {
                minDistance = difference;
                minIndex = index;
            }
        }

        return minIndex;
    }

}
