package hr.fer.oprpp1.custom.collections;

/**
 * Class which has pairs of keys and values.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class Dictionary<K, V> {

    // variable ArrayIndexedCollection used for storing DictionaryEntry
    private final ArrayIndexedCollection<DictionaryEntry<K, V>> dictionary = new ArrayIndexedCollection<>();

    /**
     * Private Class of Dictionary which holds a pair of key and value.
     *
     * @param <K> key type
     * @param <V> value type
     */
    private static class DictionaryEntry<K, V> {
        private final K key;
        private V value;

        public DictionaryEntry(K key, V value) {
            if (key == null)
                throw new IllegalArgumentException("Key of dictionary can't be null");

            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "DictionaryEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    /**
     * Check if Dictionary is empty.
     *
     * @return <code>true</code> if empty, <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return dictionary.isEmpty();
    }

    /**
     * Returns number of entries in Dictionary.
     *
     * @return number of entries in Dictionary
     */
    public int size() {
        return dictionary.size();
    }

    /**
     * Clears the Dictionary.
     */
    public void clear() {
        dictionary.clear();
    }

    /**
     * Adds new or "overwrites" current DictionaryEntry. Key of new entry can't be null. Value of new entry can be null.
     * If there was an entry with given key, method returns value of "overwritten" entry. If there wasn't an entry with
     * given key, method returns null.
     *
     * @param key   of new DictionaryEntry
     * @param value of new DictionaryEntry
     * @return value of "overwritten" entry if there was an entry with given key, null otherwise
     */
    public V put(K key, V value) {
        ElementsGetter<DictionaryEntry<K, V>> iterator = dictionary.createElementsGetter();
        V oldValue = null;

        while (iterator.hasNextElement()) {
            DictionaryEntry<K, V> entry = iterator.getNextElement();

            if (entry.getKey().equals(key)) {
                oldValue = entry.getValue();
                dictionary.remove(entry);
                break;
            }
        }

        dictionary.add(new DictionaryEntry<>(key, value));
        return oldValue;
    }

    /**
     * If there is an entry with given key, returns its value, otherwise returns null.
     *
     * @param key search dictionary entry with this key
     * @return value of an entry with given key if there is an entry with such key, null otherwise
     */
    public V get(Object key) {
        ElementsGetter<DictionaryEntry<K, V>> iterator = dictionary.createElementsGetter();

        while (iterator.hasNextElement()) {
            DictionaryEntry<K, V> entry = iterator.getNextElement();

            if (entry.getKey().equals(key))
                return entry.getValue();
        }

        return null;
    }

    /**
     * If there is an entry with given key, remove that entry and return its value, otherwise return null.
     *
     * @param key search dictionary entry with this key
     * @return value of removed entry if there was one, null otherwise
     */
    public V remove(K key) {
        ElementsGetter<DictionaryEntry<K, V>> iterator = dictionary.createElementsGetter();

        while (iterator.hasNextElement()) {
            DictionaryEntry<K, V> entry = iterator.getNextElement();

            if (entry.getKey().equals(key)) {
                V oldValue = get(entry.getKey());
                dictionary.remove(entry);
                return oldValue;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "dictionary=" + dictionary +
                '}';
    }
}
