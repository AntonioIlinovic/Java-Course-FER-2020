package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ArrayIndexedCollectionTest {

	private ArrayIndexedCollection getArrayWithFiveElements() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(0);
		array.add(1);
		array.add(2);
		array.add(3);
		array.add(4);
		return array;
	}

	@Test
	public void testInsertShiftsElements() {
		ArrayIndexedCollection result = getArrayWithFiveElements();
		result.insert("Inserted", 2);
		ArrayIndexedCollection expected = new ArrayIndexedCollection();
		expected.add(0);
		expected.add(1);
		expected.add("Inserted");
		expected.add(2);
		expected.add(3);
		expected.add(4);
		assertArrayEquals(expected.toArray(), result.toArray());
	}

	@Test
	public void testConstructorGivenInitialCapacityLessThanOneThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
	}

	@Test
	public void testConstructorGivenNullReferenceThrowsNullReferenceException() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}

	@Test
	public void testConstructorGivenOtherCollection() {
		ArrayIndexedCollection otherArray = getArrayWithFiveElements();
		ArrayIndexedCollection array = new ArrayIndexedCollection(otherArray, 2);
		assertEquals(5, array.size());
	}
	
	@Test
	public void testToArray() {
		ArrayIndexedCollection collection = getArrayWithFiveElements();
		Object[] array = new Object[] {0, 1, 2, 3, 4};
		assertArrayEquals(array, collection.toArray());
	}
	
	@Test
	public void testAddNullReferenceThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			ArrayIndexedCollection array = new ArrayIndexedCollection(5);
			array.add(null);
		});
	}

	@Test
	public void testAddInsertRemoveClearChangesArraySize() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		assertEquals(0, array.size());
		array.add("String to add to array");
		array.add("Remove me by index");
		array.add(5);
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
		ArrayIndexedCollection array = getArrayWithFiveElements();
		assertEquals(5, array.size());
	}

	@Test
	public void testGetIndexOutOfBoundsException() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(array.size()));
	}

	@Test
	public void testGetMethod() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(1);
		array.add(2);
		array.add("Third");
		array.add(4);
		assertEquals("Third", array.get(2));
	}
	
	@Test
	public void testInsertAndRemoveMovesElementsAsItShould() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
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
		ArrayIndexedCollection array = new ArrayIndexedCollection(2);
		array.add(0);
		array.add(1);
		assertDoesNotThrow(() -> array.remove(1));
	}

	@Test
	public void testInsertMethodInvalidArguments() {
		ArrayIndexedCollection array = getArrayWithFiveElements();
		assertThrows(IllegalArgumentException.class, () -> array.insert(null, 0));
	}

	@Test
	public void testIndexOfGivenNullReference() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		assertEquals(-1, array.indexOf(null));
	}
	
	@Test
	public void testIndexOfGivenObjectThatIsInside() {
		ArrayIndexedCollection array = getArrayWithFiveElements();
		assertEquals(3, array.indexOf(3));
	}
	
	@Test
	public void testIndexOfGivenObjectThatIsNotInside() {
		ArrayIndexedCollection array = getArrayWithFiveElements();
		assertEquals(-1, array.indexOf(10));
	}
	
}
