package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Dictionary;

public class DictionaryDemo {

    public static void main(String[] args) {
        Dictionary<Integer, String> dictionary = getDictionary();
        dictionary.put(10, "Ten");
        dictionary.put(5, "new Five");
    }

    private static Dictionary<Integer, String> getDictionary() {
        Dictionary<Integer, String> dictionary = new Dictionary<>();
        dictionary.put(1, "One");
        dictionary.put(2, "Two");
        dictionary.put(3, "Three");
        dictionary.put(4, "Four");
        dictionary.put(5, "Five");
        return dictionary;
    }

}
