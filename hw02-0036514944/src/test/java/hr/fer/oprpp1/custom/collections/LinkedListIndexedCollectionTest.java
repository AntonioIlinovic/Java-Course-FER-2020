package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest {

    private static LinkedListIndexedCollection getLinkedList() {
        LinkedListIndexedCollection linkedList = new LinkedListIndexedCollection();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.add(4);
        return linkedList;
    }

    private static ArrayIndexedCollection getArray() {
        ArrayIndexedCollection array = new ArrayIndexedCollection();
        array.add("a");
        array.add("b");
        array.add("c");
        array.add("d");
        return array;
    }

    @Test
    public void testLinkedListIndexedCollectionConstructorWithLinkedListArgument() {
        LinkedListIndexedCollection argument = getLinkedList();
        LinkedListIndexedCollection result = new LinkedListIndexedCollection(argument);
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
        assertEquals(4, result.get(3));
    }

    @Test
    public void testLinkedListIndexedCollectionConstructorWithArrayArgument() {
        ArrayIndexedCollection argument = getArray();
        LinkedListIndexedCollection result = new LinkedListIndexedCollection(argument);
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
        assertEquals("d", result.get(3));
    }

    @Test
    public void testSizeMethod() {
        LinkedListIndexedCollection linkedList1 = getLinkedList();
        LinkedListIndexedCollection linkedList2 = getLinkedList();
        linkedList1.addAll(linkedList2);
        linkedList1.add("END");
        assertEquals(9, linkedList1.size());
    }

    @Test
    public void testAddMethodWithValidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        list.add(5);
        list.add(6);
        list.add(7);
        assertEquals(6, list.get(5));
    }

    @Test
    public void testAddMethodWithInvalidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    public void testGetMethodWithValidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        assertEquals(2, list.get(1));
    }

    @Test
    public void testGetMethodWithInvalidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(list.size()));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    public void testContainsMethod() {
        LinkedListIndexedCollection list = getLinkedList();
        assertTrue(list.contains(2));
        assertFalse(list.contains("DOES NOT CONTAIN"));
    }

    @Test
    public void testToArrayMethod() {
        LinkedListIndexedCollection list = getLinkedList();
        Object[] listToArray = list.toArray();
        assertEquals(1, listToArray[0]);
        assertEquals(2, listToArray[1]);
        assertEquals(3, listToArray[2]);
        assertEquals(4, listToArray[3]);
    }

    @Test
    public void testClearMethod() {
        LinkedListIndexedCollection list = getLinkedList();
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    public void testInsertMethodWithValidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        list.insert("Inserted at 0", 0);
        assertEquals("Inserted at 0", list.get(0));
        list.insert("Inserted at size", list.size());
        assertEquals("Inserted at size", list.get(list.size() - 1));
        assertEquals(4, list.get(4));
        list.insert("Inserted at 2", 2);
        assertEquals("Inserted at 2", list.get(2));
    }

    @Test
    public void testInsertMethodWithInvalidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert("-1", -1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert("size", list.size() + 1));
        assertThrows(NullPointerException.class, () -> list.insert(null, 2));
    }

    @Test
    public void testIndexOfMethod() {
        LinkedListIndexedCollection list = getLinkedList();
        assertEquals(-1, list.indexOf(0));
        assertEquals(1, list.indexOf(2));
        assertEquals(3, list.indexOf(4));
    }

    @Test
    public void testRemoveWithValidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        list.remove(3);
        list.remove(1);
        list.remove(0);
        assertEquals(3, list.get(0));

        LinkedListIndexedCollection listWithOneElement = new LinkedListIndexedCollection();
        listWithOneElement.add(0);
        listWithOneElement.remove(0);
        assertTrue(listWithOneElement.isEmpty());
    }

    @Test
    public void testRemoveWithInvalidArguments() {
        LinkedListIndexedCollection list = getLinkedList();
        assertThrows(IllegalArgumentException.class, () -> list.remove(list.size()));
        assertThrows(IllegalArgumentException.class, () -> list.remove(-1));
        list.clear();
        assertThrows(RuntimeException.class, () -> list.remove(0));
    }
}
