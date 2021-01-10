package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest {

	private ArrayIndexedCollection<Integer> getIntegerArrayWithFiveElements() {
		ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>();
		array.add(0);
		array.add(1);
		array.add(2);
		array.add(3);
		array.add(4);
		return array;
	}

	private ArrayIndexedCollection<String> getStringArrayWithFiveElements() {
		ArrayIndexedCollection<String> array = new ArrayIndexedCollection<>();
		array.add("Zero");
		array.add("One");
		array.add("Two");
		array.add("Three");
		array.add("Five");
		return array;
	}

	@Test
	public void testInsertShiftsElements() {
		ArrayIndexedCollection<Integer> result = getIntegerArrayWithFiveElements();
		result.insert(100, 2);
		ArrayIndexedCollection<Integer> expected = new ArrayIndexedCollection<>();
		expected.add(0);
		expected.add(1);
		expected.add(100);
		expected.add(2);
		expected.add(3);
		expected.add(4);
		assertArrayEquals(expected.toArray(), result.toArray());
	}

	@Test
	public void testConstructorGivenInitialCapacityLessThanOneThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<Integer>(0));
	}

	@Test
	public void testConstructorGivenNullReferenceThrowsNullReferenceException() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<Integer>(null));
	}

	@Test
	public void testConstructorGivenOtherCollection() {
		ArrayIndexedCollection<Integer> otherArray = getIntegerArrayWithFiveElements();
		ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<Integer>(otherArray, 2);
		assertEquals(5, array.size());
	}
	
	@Test
	public void testToArray() {
		ArrayIndexedCollection<Integer> collection = getIntegerArrayWithFiveElements();
		Object[] array = new Object[] {0, 1, 2, 3, 4};
		assertArrayEquals(array, collection.toArray());
	}
	
	@Test
	public void testAddNullReferenceThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>(5);
			array.add(null);
		});
	}

	@Test
	public void testAddInsertRemoveClearChangesArraySize() {
		ArrayIndexedCollection<String> array = new ArrayIndexedCollection<>();
		assertEquals(0, array.size());
		array.add("String to add to array");
		array.add("Remove me by index");
		array.add("5");
		assertEquals(3, array.size());
		array.remove(1);
		assertEquals(2, array.size());
		array.insert("Position 0", 0);
		assertEquals(3, array.size());
		array.clear();
		assertEquals(0, array.size());
	}

	@Test
	public void testAddDoublesArrayCapacity() {
		ArrayIndexedCollection<Integer> array = getIntegerArrayWithFiveElements();
		assertEquals(5, array.size());
	}

	@Test
	public void testGetIndexOutOfBoundsException() {
		ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>();
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(array.size()));
	}

	@Test
	public void testGetMethod() {
		ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>();
		array.add(1);
		array.add(2);
		array.add(100);
		array.add(4);
		assertEquals(100, array.get(2));
	}
	
	@Test
	public void testInsertAndRemoveMovesElementsAsItShould() {
		ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>();
		array.add(1);
		array.add(3);
		array.add(4);
		array.add(5);
		array.insert(2, 1);
		assertEquals(2, array.get(1));
		array.remove(2);
		assertEquals(4, array.get(2));
	}

	@Test
	public void testMethodInsertEdgeCase() {
		ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>(2);
		array.add(0);
		array.add(1);
		assertDoesNotThrow(() -> array.remove(1));
	}

	@Test
	public void testInsertMethodInvalidArguments() {
		ArrayIndexedCollection<Integer> array = getIntegerArrayWithFiveElements();
		assertThrows(IllegalArgumentException.class, () -> array.insert(null, 0));
	}

	@Test
	public void testIndexOfGivenNullReference() {
		ArrayIndexedCollection<Integer> array = new ArrayIndexedCollection<>();
		assertEquals(-1, array.indexOf(null));
	}
	
	@Test
	public void testIndexOfGivenObjectThatIsInside() {
		ArrayIndexedCollection<Integer> array = getIntegerArrayWithFiveElements();
		assertEquals(3, array.indexOf(3));
	}
	
	@Test
	public void testIndexOfGivenObjectThatIsNotInside() {
		ArrayIndexedCollection<Integer> array = getIntegerArrayWithFiveElements();
		assertEquals(-1, array.indexOf(10));
	}
	
}
