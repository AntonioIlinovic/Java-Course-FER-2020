package hr.fer.oprpp1.math;

/**
 * Class which just has methods that convert from radians to degrees and vice versa.
 */
public class AngleConverter {

    /**
     * Helper method which converts from radians to degrees.
     * @param radians radians
     * @return given radians in degrees
     */
    public static double RadiansToDegrees(double radians) {
        return radians * (180.0 / Math.PI);
    }

    /**
     * Helper method which converts from degrees to radians.
     * @param degrees degrees
     * @return given degrees in radians
     */
    public static double DegreesToRadians(double degrees) {
        return degrees / 180.0 * Math.PI;
    }

}
