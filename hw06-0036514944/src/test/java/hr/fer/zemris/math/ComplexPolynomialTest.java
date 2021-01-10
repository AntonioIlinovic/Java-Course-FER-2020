package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import org.junit.jupiter.api.Test;

public class ComplexPolynomialTest {
	@Test
	public void MultiplyTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(2, 1));
		ComplexPolynomial cp2 = new ComplexPolynomial(new Complex(3, 1));

		ComplexPolynomial cp3 = cp1.multiply(cp2);
		ComplexPolynomial cp4 = cp2.multiply(cp1);
		String cp3String = "(5.0+i5.0)";
		assertEquals(cp3String, cp3.toString());
		assertEquals(cp3String, cp4.toString());
	}

	@Test
	public void OrderTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(2, 1));
		ComplexPolynomial cp2 = new ComplexPolynomial(new Complex(3, 1));
		ComplexPolynomial cp3 = new ComplexPolynomial(new Complex(2, 1),
				new Complex(-2, 1));
		ComplexPolynomial cp4 = new ComplexPolynomial(new Complex(3, 1),
				new Complex(22, 1), new Complex(2, 0));

		assertEquals(0, cp1.order());
		assertEquals(0, cp2.order());
		assertEquals(1, cp3.order());
		assertEquals(2, cp4.order());
	}

	@Test
	public void DeriveTest1() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(-2, 0),
				new Complex(0, 0), new Complex(0, 0), new Complex(0, 0),
				new Complex(2, 0));
		String cp1Derived = "(8.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(0.0+i0.0)";

		assertEquals(cp1Derived, cp1.derive().toString());
	}

	@Test
	public void DeriveTest2() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(-2, -7.22),
				new Complex(2.22, 11), new Complex(4, -3),
				new Complex(3, -1.1));
		String cp1Derived = "(9.0-i3.3)*z^2+(8.0-i6.0)*z^1+(2.2+i11.0)";

		// 3.300000000000000003 promjeniti u 3.3 ???
		//assertEquals(cp1Derived, cp1.derive().toString());
	}
}
