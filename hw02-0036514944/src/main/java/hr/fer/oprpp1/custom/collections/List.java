package hr.fer.oprpp1.custom.collections;

/**
 * Class which is more specific than Collection, thus it has more methods.
 */
public interface List extends Collection {

    Object get(int index);

    void insert(Object value, int position);

    int indexOf(Object value);

    void remove(int index);

}
