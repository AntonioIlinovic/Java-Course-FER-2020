package hr.fer.oprpp1.collections;

/**
 * Class which is more specific than Collection, thus it has more methods..
 * Parametrized by T.
 */
public interface List<T> extends Collection<T> {

    T get(int index);

    void insert(T value, int position);

    int indexOf(Object value);

    void remove(int index);

}
