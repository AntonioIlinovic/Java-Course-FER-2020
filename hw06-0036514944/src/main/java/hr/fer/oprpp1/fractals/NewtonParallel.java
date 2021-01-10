package hr.fer.oprpp1.fractals;

import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Program NewtonParallel. It can be started from command line with following syntax:
 * <p>{java hr.fer.oprpp1.fractals.NewtonParallel --workers=2 --tracks=10}</p>
 * <p>The number of threads to use for parallelization is controlled with --workers=N; alternatively, shorter form can
 * be user: -w N. If this parameter is not given, all available processors on the computer are used
 * (Runtime.getRuntime()).</p>
 * <p>The number of tracks (i.e. jobs) to be used is specified with --tracks=K (or shorter: -t K). Minimal acceptable K
 * is 1. If user specifies K which is larger than the number of rows in picture, "silently" use number of rows for
 * number of jobs. If this parameter is not present, use 4*numberOfAvailableProcessors jobs.</p>
 * <p>Please note, all parameters are optional (since default values are defined); each parameter can be specified
 * using two different syntax; the order in which parameters appear is not important. Finally, it is error to specify
 * the same parameter more than once (even if the value is the same).</p>
 * <p>The program asks user to enter roots. General syntax for complex numbers is of form
 * a+ib or a-ib where parts that are zero can be dropped, but not both (empty string is not legal complex number). For
 * example, zero can be given as 0, i0, 0+i0, 0-i0. If there is 'i' present but no b is given, you must assume that b=1.
 * After roots are entered fractal viewer is started and it displays the fractal. Implementation is sequential
 * (non-multithreaded).</p>
 */
public class NewtonParallel {

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
    public NewtonParallel(ComplexRootedPolynomial crp) {
        this.rootedPolynomial = crp;
        this.polynomial = crp.toComplexPolynom();
        this.derived = this.polynomial.derive();
    }


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
        FractalViewer.show(new Newton.MojProducer(crp));
    }


    public class PosaoIzracuna implements Runnable {
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int yMin;
        int yMax;
        int m;
        short[] data;
        AtomicBoolean cancel;
        public PosaoIzracuna NO_JOB = new PosaoIzracuna();

        /**
         * Private constructor for PosaoIzracuna, it is used just as a "poison pill" to "kill" the threads, after all
         * work is done.
         */
        private PosaoIzracuna() {
        }

        /**
         * Constructor for new work that one Thread will do.
         *
         * @param reMin lowest real value on display
         * @param reMax highest real value on display
         * @param imMin lowest imaginary value on display
         * @param imMax highest imaginary value on display
         * @param width of display
         * @param height of display
         * @param yMin row from which Thread will do the work
         * @param yMax row to which Thread will do the work
         * @param m parameter how many iterations to test if diverges
         * @param data number of iterations that it first diverged for each pixel of display
         * @param cancel if work should be canceled
         */
        public PosaoIzracuna(double reMin, double reMax, double imMin,
                             double imMax, int width, int height,
                             int yMin, int yMax, int m, short[] data,
                             AtomicBoolean cancel) {
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.cancel = cancel;
        }

        @Override
        public void run() {

            int offset = yMin * width;

            for (int y = yMin; y < yMax; y++) {
                if (cancel.get()) break;
                for (int x = 0; x < width; x++) {

                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim);
                    int iter = 0;
                    double module = 0;

                    do {
                        Complex znold = zn;

                        Complex numerator = NewtonParallel.this.polynomial.apply(zn);
                        Complex denominator = derived.apply(zn);
                        Complex fraction = numerator.divide(denominator);
                        zn = zn.sub(fraction);
                        module = znold.sub(zn).module();

                        iter++;
                    } while (module > CONVERGENCE_THRESHOLD && iter < m);

                    int index = rootedPolynomial.indexOfClosestRootFor(zn, ROOT_DISTANCE_THRESHOLD);
                    data[offset++] = (short) (index + 1);
                }
            }

        }

    }

    public class MojProducer implements IFractalProducer {

        private final PosaoIzracuna posaoIzracuna = new PosaoIzracuna();

        // Number of tracks (jobs) and workers (threads). These are default values
        private int TRACKS = 4 * Runtime.getRuntime().availableProcessors();
        private int WORKERS = Runtime.getRuntime().availableProcessors();

        public void setTracks(int tracks) {
            this.TRACKS = tracks;
        }

        public void setWorkers(int workers) {
            this.WORKERS = workers;
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
                            long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Započinjem izračun...");
            int m = 16 * 16 * 16;
            short[] data = new short[width * height];
            final int brojTraka = TRACKS;
            int brojYPoTraci = height / TRACKS;

            final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

            // Describe what each Thread will do
            Thread[] radnici = new Thread[WORKERS];
            for (int i = 0; i < radnici.length; i++) {
                radnici[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            PosaoIzracuna p = null;
                            try {
                                p = queue.take();
                                if (p == posaoIzracuna.NO_JOB) break;
                            } catch (InterruptedException e) {
                                continue;
                            }
                            p.run();
                        }
                    }
                });
            }

            // Start all Threads
            for (Thread thread : radnici) {
                thread.start();
            }

            // Give jobs to Threads
            for (int i = 0; i < brojTraka; i++) {
                int yMin = i * brojYPoTraci;
                int yMax = (i + 1) * brojYPoTraci - 1;
                if (i == brojTraka - 1)
                    yMax = height - 1;
                PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel);

                while (true) {
                    try {
                        queue.put(posao);
                        break;
                    } catch (InterruptedException ignored) {

                    }
                }
            }

            // "Poison" finished Threads
            for (int i = 0; i < radnici.length; i++) {
                while (true) {
                    try {
                        queue.put(posaoIzracuna.NO_JOB);
                        break;
                    } catch (InterruptedException ignored) {

                    }
                }
            }

            // Wait for all Threads to finish
            for (Thread thread : radnici) {
                while (true) {
                    try {
                        thread.join();
                        break;
                    } catch (InterruptedException ignored) {

                    }
                }
            }

            // Show the results
            System.out.println("Računanje gotovo. Idem obavijestiti promatrača tj. GUI!");
            observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);

        }
    }

}
