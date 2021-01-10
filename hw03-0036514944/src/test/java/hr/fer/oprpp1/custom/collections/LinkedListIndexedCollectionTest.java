package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest {

    private static LinkedListIndexedCollection<Integer> getIntegerLinkedListWithFourElements() {
        LinkedListIndexedCollection<Integer> linkedList = new LinkedListIndexedCollection<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.add(4);
        return linkedList;
    }

    private static ArrayIndexedCollection<Integer> getIntegerArrayWithFourElements() {
        ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>();
        array.add(-1);
        array.add(-2);
        array.add(-3);
        array.add(-4);
        return array;
    }

    @Test
    public void testLinkedListIndexedCollectionConstructorWithLinkedListArgument() {
        LinkedListIndexedCollection<Integer> argument = getIntegerLinkedListWithFourElements();
        LinkedListIndexedCollection<Integer> result = new LinkedListIndexedCollection(argument);
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
        assertEquals(4, result.get(3));
    }

    @Test
    public void testLinkedListIndexedCollectionConstructorWithArrayArgument() {
        ArrayIndexedCollection<Integer> argument = getIntegerArrayWithFourElements();
        LinkedListIndexedCollection<Integer> result = new LinkedListIndexedCollection<>(argument);
        assertEquals(-1, result.get(0));
        assertEquals(-2, result.get(1));
        assertEquals(-3, result.get(2));
        assertEquals(-4, result.get(3));
    }

    @Test
    public void testSizeMethod() {
        LinkedListIndexedCollection<Integer> linkedList1 = getIntegerLinkedListWithFourElements();
        LinkedListIndexedCollection<Integer> linkedList2 = getIntegerLinkedListWithFourElements();
        linkedList1.addAll(linkedList2);
        linkedList1.add(100);
        assertEquals(9, linkedList1.size());
    }

    @Test
    public void testAddMethodWithValidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        list.add(5);
        list.add(6);
        list.add(7);
        assertEquals(6, list.get(5));
    }

    @Test
    public void testAddMethodWithInvalidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    public void testGetMethodWithValidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        assertEquals(2, list.get(1));
    }

    @Test
    public void testGetMethodWithInvalidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(list.size()));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    public void testContainsMethod() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        assertTrue(list.contains(2));
        assertFalse(list.contains("DOES NOT CONTAIN"));
    }

    @Test
    public void testToArrayMethod() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        Object[] listToArray = list.toArray();
        assertEquals(1, listToArray[0]);
        assertEquals(2, listToArray[1]);
        assertEquals(3, listToArray[2]);
        assertEquals(4, listToArray[3]);
    }

    @Test
    public void testClearMethod() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    public void testInsertMethodWithValidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        list.insert(100, 0);
        assertEquals(100, list.get(0));
        list.insert(250, list.size());
        assertEquals(250, list.get(list.size() - 1));
        assertEquals(4, list.get(4));
        list.insert(200, 2);
        assertEquals(200, list.get(2));
    }

    @Test
    public void testInsertMethodWithInvalidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(-1, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(1000, list.size() + 1));
        assertThrows(NullPointerException.class, () -> list.insert(null, 2));
    }

    @Test
    public void testIndexOfMethod() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        assertEquals(-1, list.indexOf(0));
        assertEquals(1, list.indexOf(2));
        assertEquals(3, list.indexOf(4));
    }

    @Test
    public void testRemoveWithValidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        list.remove(3);
        list.remove(1);
        list.remove(0);
        assertEquals(3, list.get(0));

        LinkedListIndexedCollection<Integer> listWithOneElement = new LinkedListIndexedCollection<>();
        listWithOneElement.add(0);
        listWithOneElement.remove(0);
        assertTrue(listWithOneElement.isEmpty());
    }

    @Test
    public void testRemoveWithInvalidArguments() {
        LinkedListIndexedCollection<Integer> list = getIntegerLinkedListWithFourElements();
        assertThrows(IllegalArgumentException.class, () -> list.remove(list.size()));
        assertThrows(IllegalArgumentException.class, () -> list.remove(-1));
        list.clear();
        assertThrows(RuntimeException.class, () -> list.remove(0));
    }

}
