package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import org.junit.jupiter.api.Test;

public class ComplexRootedPolynomialTest {
	@Test
	public void GivenExamplesTest() {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
				new Complex(2, 0), Complex.ONE, Complex.ONE_NEG, Complex.IM,
				Complex.IM_NEG);
		String crpString = "(2.0+i0.0)*(z-(1.0+i0.0))*(z-(-1.0+i0.0))*(z-(0.0+i1.0))*(z-(0.0-i1.0))";
		assertEquals(crpString, crp.toString());
	}

	@Test
	public void ConversionToComplexPolynomialTest() {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
				new Complex(2, 0), Complex.ONE, Complex.ONE_NEG, Complex.IM,
				Complex.IM_NEG);
		ComplexPolynomial cp = crp.toComplexPolynom();
		String cpString = "(2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0)";
		assertEquals(cpString, cp.toString());
	}
}
