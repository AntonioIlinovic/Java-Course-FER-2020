package hr.fer.oprpp1.fractals;

import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.mandelbrot.Mandelbrot;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Program Newton. The program asks user to enter roots. General syntax for complex numbers is of form a+ib or a-ib
 * where parts that are zero can be dropped, but not both (empty string is not legal complex number). For example, zero
 * can be given as 0, i0, 0+i0, 0-i0. If there is 'i' present but no b is given, you must assume that b=1. After roots
 * are entered fractal viewer is started and it displays the fractal. Implementation is sequential (non-multithreaded).
 */
public class Newton {

    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        Scanner sc = new Scanner(System.in);
        String line;
        int noOfRoot = 1;
        List<Complex> roots = new ArrayList<>();

        do {
            System.out.printf("Root %d> ", noOfRoot++);
            line = sc.nextLine();

            if (line.equalsIgnoreCase("done"))
                break;

            roots.add(Complex.parse(line));
        } while (true);

        Complex[] rootsArray = roots.toArray(new Complex[0]);
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(Complex.ONE, rootsArray);

        System.out.println("Image of fractal will appear shortly. Thank you.");
        FractalViewer.show(new MojProducer(crp));
    }



    public static class MojProducer implements IFractalProducer {

        private final double ROOT_DISTANCE_THRESHOLD = 0.002;
        private final double CONVERGENCE_THRESHOLD = 0.001;

        private ComplexRootedPolynomial rootedPolynomial;
        private ComplexPolynomial polynomial;
        private ComplexPolynomial derived;

        /**
         * Constructor with ComplexRootedPolynomial.
         *
         * @param crp user entered ComplexRootedPolynomial
         */
        public MojProducer(ComplexRootedPolynomial crp) {
            this.rootedPolynomial = crp;
            this.polynomial = crp.toComplexPolynom();
            this.derived = this.polynomial.derive();
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
                            long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Starting calculation...");

            int MAX_ITER = 16 * 16 * 16;
            int offset = 0;
            short[] data = new short[width * height];

            for (int y = 0; y < height; y++) {
                if (cancel.get()) break;
                for (int x = 0; x < width; x++) {

                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim);
                    int iter = 0;
                    double module = 0;

                    do {
                        Complex znold = zn;

                        Complex numerator = polynomial.apply(zn);
                        Complex denominator = derived.apply(zn);
                        Complex fraction = numerator.divide(denominator);
                        zn = zn.sub(fraction);
                        module = znold.sub(zn).module();

                        iter++;
                    } while (module > CONVERGENCE_THRESHOLD && iter < MAX_ITER);

                    int index = rootedPolynomial.indexOfClosestRootFor(zn, ROOT_DISTANCE_THRESHOLD);
                    data[offset++] = (short) (index + 1);
                }
            }

            System.out.println("Calculation finished. Notifying observer (GUI)!");
            observer.acceptResult(data, (short) (polynomial.order()+1), requestNo);
        }
    }

}
