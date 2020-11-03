package hr.fer.oprpp1.collections;

/**
 * Interface which defines methods needed for a List.
 * Parametrized by T.
 */
public interface List<T> extends Collection<T> {

    T get(int index);

    void insert(T value, int position);

    int indexOf(Object value);

    void remove(int index);

}
