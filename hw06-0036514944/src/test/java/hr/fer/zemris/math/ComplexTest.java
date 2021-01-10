package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hr.fer.oprpp1.math.Complex;
import org.junit.jupiter.api.Test;

public class ComplexTest {

	@Test
	public void RealTest() {
		Complex c1 = new Complex(2, 0);
		Complex c2 = new Complex(4, 0);
		Complex c3 = new Complex(-1.44, 0);

		Complex r1 = c1.add(c2).sub(c3);
		Complex r2 = c1.sub(c2).multiply(c3);
		Complex r3 = c1.multiply(c2).divide(c2);

		Complex result = new Complex(7.44, 0);
		assertEquals(result.getReal(), r1.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r1.getImaginary(), 0.0001);

		result = new Complex(2.88, 0);
		assertEquals(result.getReal(), r2.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r2.getImaginary(), 0.0001);

		result = new Complex(2, 0);
		assertEquals(result.getReal(), r3.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r3.getImaginary(), 0.0001);
	}

	@Test
	public void ImaginaryTest() {
		Complex c1 = new Complex(0, 0);
		Complex c2 = new Complex(0, -4);
		Complex c3 = new Complex(0, 3.333);

		Complex r1 = c1.add(c2).sub(c3);
		Complex r2 = c1.sub(c2).multiply(c3);
		Complex r3 = c1.multiply(c2).divide(c2);

		assertEquals(new Complex(0, -7.333), r1);
		assertEquals(new Complex(-13.332, 0), r2);
		assertEquals(new Complex(0, 0), r3);
	}

	@Test
	public void BasicOperationsTest() {
		Complex c1 = new Complex(2, 0);
		Complex c2 = new Complex(0, 3.14);
		Complex c3 = new Complex(12, 0.88);
		Complex c4 = new Complex(-1.01, -1.01);

		Complex r1 = c1.add(c2).sub(c3);
		Complex r2 = c1.sub(c2).multiply(c3);
		Complex r3 = c1.multiply(c2).divide(c2);
		Complex r4 = c3.multiply(c1).multiply(c2);
		Complex r5 = c1.multiply(c2).multiply(c3).divide(c4).sub(c4);

		Complex result = new Complex(-10, 2.26);
		assertEquals(result.getReal(), r1.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r1.getImaginary(), 0.0001);

		result = new Complex(26.7632, -35.92);
		assertEquals(result.getReal(), r2.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r2.getImaginary(), 0.0001);

		result = new Complex(2, 0);
		assertEquals(result.getReal(), r3.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r3.getImaginary(), 0.0001);

		result = new Complex(-5.5264, 75.36);
		assertEquals(result.getReal(), r4.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r4.getImaginary(), 0.0001);

		result = new Complex(-33.56108911, -39.03277228);
		assertEquals(result.getReal(), r5.getReal(), 0.0001);
		assertEquals(result.getImaginary(), r5.getImaginary(), 0.0001);
	}

	@Test
	public void AdvancedOperationsTest() {
		// TODO write it
	}

	@Test
	public void ParseTest1() {
		Complex c1 = Complex.parse("1");
		assertEquals(new Complex(1, 0), c1);

		Complex c2 = Complex.parse("-1 + i0");
		assertEquals(new Complex(-1, 0), c2);

		Complex c3 = Complex.parse("i");
		assertEquals(new Complex(0, 1), c3);

		Complex c4 = Complex.parse("0 - i1");
		assertEquals(new Complex(0, -1), c4);
	}

	@Test
	public void ParseTest2() {
		Complex c1 = Complex.parse("3.33 -i");
		assertEquals(new Complex(3.33, -1), c1);

		Complex c2 = Complex.parse("-1.2 + i0.5");
		assertEquals(new Complex(-1.2, 0.5), c2);

		Complex c3 = Complex.parse("-i2.22");
		assertEquals(new Complex(0, -2.22), c3);

		Complex c4 = Complex.parse("2 - i1.44");
		assertEquals(new Complex(2, -1.44), c4);
	}

	@Test
	public void ParseTest3() {
		Complex c1 = Complex.parse("0");
		assertEquals(new Complex(0, 0), c1);

		Complex c2 = Complex.parse("i0");
		assertEquals(new Complex(0, 0), c2);

		Complex c3 = Complex.parse("0+i0");
		assertEquals(new Complex(0, 0), c3);

		Complex c4 = Complex.parse("0-i0");
		assertEquals(new Complex(0, 0), c4);
	}

	@Test
	public void ParseTest4() {
		Complex c1 = Complex.parse("1");
		assertEquals(new Complex(1, 0), c1);

		Complex c2 = Complex.parse("-1");
		assertEquals(new Complex(-1, 0), c2);

		Complex c3 = Complex.parse("i");
		assertEquals(new Complex(0, 1), c3);

		Complex c4 = Complex.parse("-i");
		assertEquals(new Complex(0, -1), c4);
	}

}
