package hr.fer.oprpp1.custom.collections;

/**
 * Accepts some object and test if it is acceptable or not.
 */
public interface Tester {

    /**
     * Tests if given object is acceptable or not.
     *
     * @param obj object to be tested if acceptable
     * @return <code>true</code> if acceptable, <code>false</code> otherwise
     */
    boolean test(Object obj);

}
