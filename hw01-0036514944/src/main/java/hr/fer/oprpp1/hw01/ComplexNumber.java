package hr.fer.oprpp1.hw01;

/**
 * Class for unmodifiable complex number. Each method which performs some kind of modification returns a new instance
 * which represents modified number.
 */
public class ComplexNumber {

    private final double real;
    private final double imaginary;

    /**
     * @param real      part of complex number.
     * @param imaginary part of complex number.
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0.0);
    }

    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0.0, imaginary);
    }

    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        if (magnitude < 0.0)
            throw new IllegalArgumentException("Magnitude must be greater or equal 0.");

        return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    /**
     * Method which parses String into ComplexNumber. Accepts Strings such as: "3.51", "-3.17", "-2.71i", "i", "1",
     * "-2.71-3.15i"
     *
     * @param s String to parse.
     * @return ComplexNumber parsed.
     */
    public static ComplexNumber parse(String s) {
        double realParsed = 0.0;
        double imaginaryParsed = 0.0;

        /*
        Multiple signs are not supported. Following are not accepted: "-+2.7", "--1.5", "-2.7+-3.15i".
         */
        boolean lastCharWasSign = false;
        for (int index = 0; index < s.length(); index++) {
            if (s.charAt(index) == '+' || s.charAt(index) == '-') {
                if (lastCharWasSign) throw new IllegalArgumentException("Multiple signs are not supported.");

                lastCharWasSign = true;
            }

            lastCharWasSign = false;
        }

        String firstPart = "";
        String secondPart = "";

        /*
        This implementation of parsing ComplexNumber splits real and imaginary part by
        finding the last index of operator "+" or "-", and then splitting the string.
         */
        int indexOfLastSign = Math.max(s.lastIndexOf("+"), s.lastIndexOf("-"));
        if (indexOfLastSign == -1) { // If there is no "+" or "-" signs in parsed String.
            firstPart = s.substring(0, s.length());
            secondPart = null;
        } else if (indexOfLastSign == 0) { // If there is only one "+" or "-" sign at position 0.
            firstPart = s.substring(0, s.length());
            secondPart = null;
        } else {
            firstPart = s.substring(0, indexOfLastSign);
            secondPart = s.substring(indexOfLastSign, s.length());
        }

        /*
        If any of those two parts contains "i" in any place other than the last (0 to .length-1), throw IllegalArgumentException.
        Following are not accepted: "i33", "-i2.3".
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
        else if (firstPartIndexOf_i >= 0 && firstPartIndexOf_i < (firstPart.length() - 1))
            throw new IllegalArgumentException("Imaginary letter 'i' must be at the end of the imaginary part.");
        else if (firstPartIndexOf_i == -1)
            realParsed += Double.parseDouble(firstPart);
        else if (firstPartIndexOf_i == firstPart.length() - 1)
            imaginaryParsed += Double.parseDouble(firstPart.substring(0, firstPart.length() - 1));

        if (secondPart == null) {
        } else if (secondPart.equals("i") || secondPart.equals("+i"))
            imaginaryParsed += 1.0;
        else if (secondPart.equals("-i"))
            imaginaryParsed -= 1.0;
        else if (secondPartIndexOf_i >= 0 && secondPartIndexOf_i < (secondPart.length() - 1))
            throw new IllegalArgumentException("Imaginary letter 'i' must be at the end of the imaginary part.");
        else if (secondPartIndexOf_i == -1)
            realParsed += Double.parseDouble(secondPart);
        else if (secondPartIndexOf_i == secondPart.length() - 1)
            imaginaryParsed += Double.parseDouble(secondPart.substring(0, secondPart.length() - 1));

        return new ComplexNumber(realParsed, imaginaryParsed);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public double getMagnitude() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    /**
     * @return Angle in radians, from 0 to 2 Pi.
     */
    public double getAngle() {
        double angle = Math.atan2(imaginary, real);
        if (angle < 0.0)
            angle += 2 * Math.PI;
        return angle;
    }

    public ComplexNumber add(ComplexNumber c) {
        return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
    }

    public ComplexNumber sub(ComplexNumber c) {
        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    public ComplexNumber mul(ComplexNumber c) {
        // (a + bi) (c + di) = ac + adi + bci + bd(i^2) = ac - bd + (ad + bc)*i
        return new ComplexNumber(this.real * c.real - this.imaginary * c.imaginary, this.real * c.imaginary + this.imaginary * c.real);
    }

    public ComplexNumber div(ComplexNumber c) {
        double divisor = c.real * c.real + c.imaginary * c.imaginary; // (a-bi)(a+bi) = a*a + b*b

        double resultReal = (this.getReal() * c.getReal() + this.getImaginary() * c.getImaginary()) / divisor;
        double resultImaginary = (c.getReal() * this.getImaginary() - this.getReal() * c.getImaginary()) / divisor;

        return new ComplexNumber(resultReal, resultImaginary);
    }

    /**
     * @param n exponent value. n >= 0.
     * @return ComplexNumber to the power of n of this ComplexNumber instance.
     */
    public ComplexNumber power(int n) {
        if (n < 0) throw new IllegalArgumentException("Exponent argument must be greater or equal to 0.");

        // z^n = r^n * (cos(n * theta) + i*sin(n * theta))
        double rToThePowerN = Math.pow(getMagnitude(), n);
        double nTimesTheta = n * getAngle();
        return new ComplexNumber(rToThePowerN * Math.cos(nTimesTheta), rToThePowerN * Math.sin(nTimesTheta));
    }

    /**
     * @param n nth root of this ComplexNumber instance. n > 0.
     * @return Array of ComplexNumber which are nth roots of this ComplexNumber instance.
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) throw new IllegalArgumentException("nth root argument must be greater than 0.");

        /*
        There are n nth roots and they are:

        z1 = r^(1/n) * [ cos( (theta + 2k*PI / n) ) + i*sin( (theta + 2k*PI) / n ) ]
        k = 0, 1, ... , n-1
         */
        ComplexNumber[] roots = new ComplexNumber[n];

        double magnitudeRooted = Math.pow(getMagnitude(), 1.0 / n);
        for (int k = 0; k < n; k++) {
            double realPart = Math.cos((getAngle() + 2 * k * Math.PI) / n);
            double imaginaryPart = Math.sin((getAngle() + 2 * k * Math.PI) / n);

            roots[k] = new ComplexNumber(magnitudeRooted * realPart, magnitudeRooted * imaginaryPart);
        }

        return roots;
    }

    /**
     * @return Complex number in format: a+bi, a-bi, -a+bi, -a-bi. a and b will be in double format with a.0 and b.0
     * format.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(real);
        if (imaginary > 0.0)
            sb.append("+").append(imaginary).append("i");
        else if (imaginary < 0.0)
            sb.append(imaginary).append("i");

        return sb.toString();
    }
}
