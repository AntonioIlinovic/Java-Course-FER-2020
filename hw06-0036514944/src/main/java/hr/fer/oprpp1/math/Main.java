package hr.fer.oprpp1.math;

public class Main {

    public static void main(String[] args) {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        System.out.println(crp);

        ComplexPolynomial cp = new ComplexPolynomial(
                new Complex(-2,0), Complex.ZERO, Complex.ZERO, Complex.ZERO, new Complex(2,0)
        );
        System.out.println(cp);
    }

}
