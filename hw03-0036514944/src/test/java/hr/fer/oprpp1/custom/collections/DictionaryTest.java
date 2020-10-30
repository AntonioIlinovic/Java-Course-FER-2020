package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DictionaryTest {

    private static Dictionary<Integer, String> getDictionaryWithFiveEntries() {
        Dictionary<Integer, String> dictionary = new Dictionary<>();
        dictionary.put(1, "One");
        dictionary.put(2, "Two");
        dictionary.put(3, "Three");
        dictionary.put(4, "Four");
        dictionary.put(5, "Five");
        return dictionary;
    }

    @Test
    public void testAddMethodWithoutOverwriting() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        assertEquals(5, dictionary.size());
        dictionary.put(10, "Ten");
        assertEquals(6, dictionary.size());
    }

    @Test
    public void testAddMethodWithOverwriting() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        dictionary.put(5, "New Five");
        assertEquals(5, dictionary.size());
        assertEquals("New Five", dictionary.get(5));
    }

    @Test
    public void testGetMethodExistingKey() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        String five = dictionary.get(5);
        assertEquals("Five", five);
        String one = dictionary.get(1);
        assertEquals("One", one);
    }

    @Test
    public void testGetMethodNonExistingKey() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        String nullString = dictionary.get(15);
        assertNull(nullString);
    }

    @Test
    public void testRemoveMethodExistingKey() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        String removedValueFive = dictionary.remove(5);
        assertEquals("Five", removedValueFive);
        String removedValueOne = dictionary.remove(1);
        assertEquals("One", removedValueOne);
        assertEquals(3, dictionary.size());
    }

    @Test
    public void testRemoveMethodNonExistingKey() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        String nullString = dictionary.remove(15);
        assertNull(nullString);
        nullString = dictionary.remove(-1);
        assertNull(nullString);
        assertEquals(5, dictionary.size());
    }

    @Test
    public void testSizeMethod() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        String removedValueFive = dictionary.remove(5);
        String removedValueOne = dictionary.remove(1);
        String removedValueZero = dictionary.remove(0);
        assertEquals(3, dictionary.size());
    }

    @Test
    public void testIsEmptyMethod() {
        Dictionary<Integer, String> dictionary = new Dictionary<>();
        assertTrue(dictionary.isEmpty());
        dictionary = getDictionaryWithFiveEntries();
        assertFalse(dictionary.isEmpty());
        dictionary.clear();
        assertTrue(dictionary.isEmpty());
    }

    @Test
    public void testClearMethod() {
        Dictionary<Integer, String> dictionary = getDictionaryWithFiveEntries();
        assertFalse(dictionary.isEmpty());
        dictionary.clear();
        assertTrue(dictionary.isEmpty());
    }
}
