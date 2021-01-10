package hr.fer.oprpp1.custom.collections;

/**
 * Accepts some object and test if it is acceptable or not.
 * Parametrized by T.
 */
public interface Tester<T> {

    /**
     * Tests if given object is acceptable or not.
     *
     * @param obj of type T to be tested if acceptable
     * @return <code>true</code> if acceptable, <code>false</code> otherwise
     */
    boolean test(T obj);

}
