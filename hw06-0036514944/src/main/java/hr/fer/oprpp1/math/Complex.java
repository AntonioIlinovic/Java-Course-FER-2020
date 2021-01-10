package hr.fer.oprpp1.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for unmodifiable complex number. Each method which performs some kind of modification returns a new instance
 * which represents modified number.
 */
public class Complex {

    private final double real;
    private final double imaginary;

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(-1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, -1);

    /**
     * Empty constructor for Complex number. It initializes real and imaginary part with zeroes.
     */
    public Complex() {
        this.real = 0;
        this.imaginary = 0;
    }

    /**
     * @param real      part of complex number.
     * @param imaginary part of complex number.
     */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * @return module of complex number
     */
    public double module() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    /**
     * @param c
     * @return this * c
     */
    public Complex multiply(Complex c) {
        // (a + bi) (c + di) = ac + adi + bci + bd(i^2) = ac - bd + (ad + bc)*i
        return new Complex(this.real * c.real - this.imaginary * c.imaginary, this.real * c.imaginary + this.imaginary * c.real);
    }

    /**
     * @param c
     * @return this / c
     */
    public Complex divide(Complex c) {
        if (c.equals(Complex.ZERO))
            throw new IllegalArgumentException("You can't divide by zero!");

        double divisor = c.real * c.real + c.imaginary * c.imaginary; // (a-bi)(a+bi) = a*a + b*b

        double resultReal = (this.getReal() * c.getReal() + this.getImaginary() * c.getImaginary()) / divisor;
        double resultImaginary = (c.getReal() * this.getImaginary() - this.getReal() * c.getImaginary()) / divisor;

        return new Complex(resultReal, resultImaginary);
    }

    /**
     * @param c
     * @return this + c
     */
    public Complex add(Complex c) {
        return new Complex(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * @param c
     * @return this - c
     */
    public Complex sub(Complex c) {
        return new Complex(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * @return -this
     */
    public Complex negate() {
        return new Complex(-this.real, -this.imaginary);
    }

    /**
     * @param n raise this instance of Complex to this n, n is non-negative integer
     * @return this^n
     */
    public Complex power(int n) {
        if (n < 0) throw new IllegalArgumentException("Exponent argument must be greater or equal to 0.");

        // z^n = r^n * (cos(n * theta) + i*sin(n * theta))
        double rToThePowerN = Math.pow(module(), n);
        double nTimesTheta = n * getAngle();
        return new Complex(rToThePowerN * Math.cos(nTimesTheta), rToThePowerN * Math.sin(nTimesTheta));
    }

    /**
     * @param n nth root of this instance of Complex. n > 0
     * @return List of Complex which are nth roots of this Complex instance
     */
    public List<Complex> root(int n) {
        if (n <= 0) throw new IllegalArgumentException("nth root argument must be greater than 0.");

        /*
        There are n nth roots and they are:

        z1 = r^(1/n) * [ cos( (theta + 2k*PI / n) ) + i*sin( (theta + 2k*PI) / n ) ]
        k = 0, 1, ... , n-1
         */
        List<Complex> roots = new ArrayList<>();
        double moduleRooted = Math.pow(module(), 1.0 / n);

        for (int k = 0; k < n; k++) {
            double realPart = Math.cos((getAngle() + 2 * k * Math.PI) / n);
            double imaginaryPart = Math.sin((getAngle() + 2 * k * Math.PI) / n);

            roots.add(new Complex(moduleRooted * realPart, moduleRooted * imaginaryPart));
        }

        return roots;
    }

    /**
     * Returns angle in radians, from 0 to 2 PI.
     *
     * @return angle in radians, from 0 to 2 PI
     */
    public double getAngle() {
        double angle = Math.atan2(imaginary, real);
        if (angle < 0.0)
            angle += 2 * Math.PI;
        return angle;
    }

    /**
     * Method which parses String into ComplexNumber. Accepts Strings such as: "3.51", "-3.17", "-2.71i", "i", "1",
     * "-2.71-3.15i"
     *
     * @param s String to parse.
     * @return ComplexNumber parsed.
     */
    public static Complex parse(String s) {
        double realParsed = 0.0;
        double imaginaryParsed = 0.0;

        s = s.replaceAll(" ", "");

        /*
        Multiple signs are not supported. Following are not accepted: "-+2.7", "--1.5", "-2.7+-3.15i".
         */
        boolean lastCharWasSign = false;
        for (int index = 0; index < s.length(); index++) {
            if (s.charAt(index) == '+' || s.charAt(index) == '-') {
                if (lastCharWasSign) throw new IllegalArgumentException("Multiple signs are not supported.");

                lastCharWasSign = true;
            } else {
                lastCharWasSign = false;
            }
        }

        String firstPart = "";
        String secondPart = "";

        /*
        This implementation of parsing ComplexNumber splits real and imaginary part by
        finding the last index of operator "+" or "-", and then splitting the string.
         */
        int indexOfLastSign = Math.max(s.lastIndexOf("+"), s.lastIndexOf("-"));
        if (indexOfLastSign == -1) { // If there is no "+" or "-" signs in parsed String.
            firstPart = s;
            secondPart = null;
        } else if (indexOfLastSign == 0) { // If there is only one "+" or "-" sign at position 0.
            firstPart = s;
            secondPart = null;
        } else {
            firstPart = s.substring(0, indexOfLastSign);
            secondPart = s.substring(indexOfLastSign);
        }

        /*
        If any of those two parts contains "i" in any place other than the first or second place, throw IllegalArgumentException.
        Following are not accepted: "33i", "-i2.3".
         */
        int firstPartIndexOf_i = firstPart.indexOf("i");
        int secondPartIndexOf_i = 0;
        if (secondPart != null) {
            secondPartIndexOf_i = secondPart.indexOf("i");
        }

        /*
        Edge cases with: "i", "+i" and "-i".
        Using interface and lambda expression just so there is no duplicate code.
         */
        if (firstPart.equals("i") || firstPart.equals("+i"))
            imaginaryParsed += 1.0;
        else if (firstPart.equals("-i"))
            imaginaryParsed -= 1.0;
        else if (firstPartIndexOf_i >= 2 && firstPartIndexOf_i < (firstPart.length()))
            throw new IllegalArgumentException("Imaginary letter 'i' must be at the start of the imaginary part.");
        else if (firstPartIndexOf_i == -1)
            realParsed += Double.parseDouble(firstPart);
        else if (firstPartIndexOf_i == 0 || firstPartIndexOf_i == 1) {
            firstPart = firstPart.replaceAll("i", "");
            imaginaryParsed += Double.parseDouble(firstPart);
        }


        if (secondPart == null) {
        } else if (secondPart.equals("i") || secondPart.equals("+i"))
            imaginaryParsed += 1.0;
        else if (secondPart.equals("-i"))
            imaginaryParsed -= 1.0;
        else if (secondPartIndexOf_i == 0 || secondPartIndexOf_i >= 2 && secondPartIndexOf_i < (secondPart.length()))
            throw new IllegalArgumentException("Imaginary letter 'i' must be at the start of the imaginary part.");
        else if (secondPartIndexOf_i == -1)
            realParsed += Double.parseDouble(secondPart);
        else if (secondPartIndexOf_i == 0 || secondPartIndexOf_i == 1) {
            secondPart = secondPart.replaceAll("i", "");
            imaginaryParsed += Double.parseDouble(secondPart);
        }

        return new Complex(realParsed, imaginaryParsed);
    }

    /**
     * @return Complex number in format: a+bi, a-bi, -a+bi, -a-bi. a and b will be in double format with a.0 and b.0
     * format.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(real);
        if (imaginary >= 0.0)
            sb.append("+").append("i").append(imaginary);
        else
            sb.append("-").append("i").append(-imaginary);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.real, real) == 0 &&
                Double.compare(complex.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }
}
