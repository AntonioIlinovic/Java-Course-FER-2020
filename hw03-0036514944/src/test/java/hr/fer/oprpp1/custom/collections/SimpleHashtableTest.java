package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleHashtableTest {

    @Test
    public void testHashtable() {
        // Create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        // query collection:
        Integer kristinaGrade = examMarks.get("Kristina");
        System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
        assertEquals(5, kristinaGrade);

        // What is collection's size? Must be four!
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4
        assertEquals(4, examMarks.size());
        System.out.println(examMarks.toString());

    }

    @Test
    public void testIterator() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        assertEquals(4, examMarks.size());
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        assertEquals(4, examMarks.size());

        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }
    }

    @Test
    public void testDoubleIterator() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        StringBuilder cartesianProduct = new StringBuilder();
        for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
            for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
                System.out.printf(
                        "(%s => %d) - (%s => %d)%n",
                        pair1.getKey(), pair1.getValue(),
                        pair2.getKey(), pair2.getValue()
                );
                cartesianProduct.append("(").append(pair1.getKey()).append(" => ").append(pair1.getValue())
                        .append(") - (").append(pair2.getKey()).append(" => ").append(pair2.getValue()).append(")\n");
            }
        }

        String expected = """
                (Ante => 2) - (Ante => 2)
                (Ante => 2) - (Ivana => 5)
                (Ante => 2) - (Jasna => 2)
                (Ante => 2) - (Kristina => 5)
                (Ivana => 5) - (Ante => 2)
                (Ivana => 5) - (Ivana => 5)
                (Ivana => 5) - (Jasna => 2)
                (Ivana => 5) - (Kristina => 5)
                (Jasna => 2) - (Ante => 2)
                (Jasna => 2) - (Ivana => 5)
                (Jasna => 2) - (Jasna => 2)
                (Jasna => 2) - (Kristina => 5)
                (Kristina => 5) - (Ante => 2)
                (Kristina => 5) - (Ivana => 5)
                (Kristina => 5) - (Jasna => 2)
                (Kristina => 5) - (Kristina => 5)
                """;

        assertEquals(expected, cartesianProduct.toString());
    }

    @Test
    public void testInsertMethodAtStartTwoWithSameKey() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();

        examMarks.put("Ivana", 5);
        examMarks.put("Ivana", 3);

        assertEquals(1, examMarks.size());
        assertEquals(3, examMarks.get("Ivana"));
    }

    @Test
    public void testIteratorRemovingSafelyWithIterator() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
        assertDoesNotThrow(
                () -> {
            while (iter.hasNext()) {
                SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
                if (pair.getKey().equals("Ivana")) {
                    iter.remove(); // sam iterator kontrolirano uklanja trenutni element
                }
            }
        });
    }

    @Test
    public void testIteratorRemovingTwoTimesInARow() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        assertThrows(IllegalStateException.class, () -> {
            while (iter.hasNext()) {
                SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
                if (pair.getKey().equals("Ivana")) {
                    iter.remove();
                    iter.remove();
                }
            }
        });
    }

    @Test
    public void testIteratorRemovingWithoutIterator() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        assertThrows(ConcurrentModificationException.class, () -> {
            while(iter.hasNext()) {
                SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
                if(pair.getKey().equals("Ivana")) {
                    examMarks.remove("Ivana");
                }
            }
        });
    }

    @Test
    public void testIteratorRemovingAllelements() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana

        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            stringBuilder.append(pair.getKey()).append(" => ").append(pair.getValue()).append("\n");
            iter.remove();
        }
        System.out.printf("Veličina: %d%n", examMarks.size());
        assertTrue(examMarks.isEmpty());

        String expected = """
                Ante => 2
                Ivana => 5
                Jasna => 2
                Kristina => 5
                """;
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void testHashTablePutsValues() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);
        testTable.put("Stefek", null);

        assertEquals(2, testTable.get("Kristina"));
        assertEquals(5, testTable.get("Ivana"));
        assertEquals(6, testTable.size());
        assertNull(testTable.get("Stefek"));
        assertThrows(NullPointerException.class, () -> {
            testTable.put(null, null);
        });
    }

    @Test
    public void testHashTableToString() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(3);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana

        assertEquals("[Ante=2, Ivana=5, Jasna=2]", testTable.toString());
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip-", 100);
        testTable.put("Kristin", 2);
        assertTrue(testTable.containsKey("Kristina"));
        assertTrue(testTable.containsKey("Josip-"));
    }

    @Test
    public void testContainsValue() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        assertTrue(testTable.containsValue(100));
        assertTrue(testTable.containsValue(5));
        assertTrue(testTable.containsValue(2));
    }

    @Test
    public void testRemoveElement() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        testTable.remove("Ivana");
        testTable.remove("Josip");

        assertFalse(testTable.containsKey("Ivana"));
        assertTrue(testTable.containsKey("Jasna"));
        assertFalse(testTable.containsKey("Joža"));
        assertFalse(testTable.containsKey("Josip"));
    }

    @Test
    public void testRemoveElementMultipleInSlot() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(8);

        testTable.put("Ana", 1);
        testTable.put("Alen", 2);
        testTable.put("Dunja", 3);
        testTable.put("File", 4);
        testTable.put("Program", 5);

        testTable.remove("Ana");
        testTable.remove("Program");
        testTable.remove("Dunja");

        assertTrue(testTable.containsKey("Alen"));
        assertTrue(testTable.containsKey("File"));
        assertFalse(testTable.containsKey("Ana"));
        assertFalse(testTable.containsKey("Dunja"));
        assertFalse(testTable.containsKey("Program"));
    }

    private int calculateSlot(Object key, int slots) {
        return Math.abs(key.hashCode()) % slots;
    }

    @Test
    public void testDynamicResize() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(4);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("osim", 100);
        testTable.put("Josip", 100);
        testTable.put("Basna", 2);
        testTable.put("Bana", 2);
        assertEquals(16, testTable.getSlots());
        assertEquals(8, testTable.size());

        assertTrue(testTable.containsKey("Ivana"));
        assertTrue(testTable.containsKey("Kristina"));
        assertTrue(testTable.containsKey("Ante"));
        assertTrue(testTable.containsKey("Basna"));
        assertTrue(testTable.containsKey("Josip"));
        assertTrue(testTable.containsKey("osim"));
    }

    @Test
    public void testClear() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(4);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("osim", 100);
        testTable.put("Josip", 100);
        testTable.put("Basna", 2);
        testTable.put("Bana", 2);
        testTable.clear();
        assertEquals(16, testTable.getSlots());
        assertEquals("[]", testTable.toString());
        assertEquals(0, testTable.size());
    }

    @Test
    public void testHashtableIteratorInForEach() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        StringBuilder result = new StringBuilder();

        for (var element : testTable) {
            result.append(element.getKey()).append(element.getValue());
        }

        assertEquals("Josip100Ante2Ivana5Jasna2Kristina2", result.toString());
    }

    @Test
    public void testHashtableIteratorRemoveValid() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        var it = testTable.iterator();

        while (it.hasNext()) {
            if (it.next().getKey().equals("Ivana"))
                it.remove();
        }
        assertFalse(testTable.containsKey("Ivana"));
    }

    @Test
    public void testHashtableIteratorNextThrowsException() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);

        var it = testTable.iterator();

        it.next();
        it.next();

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void testHashtableIteratorConcurrentModificationError() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);

        var it = testTable.iterator();

        it.next();
        testTable.put("Lucija", 2);

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    public void testHashtableIteratorRemoveCalledTwiceThrowsException() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        var it = testTable.iterator();

        assertThrows(IllegalStateException.class, () -> {
            while (it.hasNext()) {
                if (it.next().getKey().equals("Ivana")) {
                    it.remove();
                    it.remove();
                }
            }
        });
    }

    @Test
    public void MainFromHomeworkTest() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(8);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        String resultString = "";
        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks)
            resultString = resultString.concat(pair.getKey().toString() + "=" + pair.getValue().toString());

        assertEquals("Ante=2Ivana=2Jasna=2Kristina=5", resultString);

    }

    @Test
    public void MainFromHomeworkTest2() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(8);
        String resultString = "";
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
            for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
                resultString = resultString.concat(pair1.getKey().toString() + "," + pair1.getValue().toString() + "<->"
                        + pair2.getKey().toString() + "," + pair2.getValue().toString());
            }
        }
        assertEquals("Ante,3<->Ante,3Ante,3<->Ivana,2Ivana,2<->Ante,3Ivana,2<->Ivana,2", resultString);
    }

    @Test
    public void testHashtableDoubleIterator() { // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2); //
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        StringBuilder sb = new StringBuilder();
        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
            sb.append(String.format("%s => %d\n", pair.getKey(), pair.getValue()));
        }

        assertEquals("Ante => 2\n" + "Ivana => 5\n" + "Jasna => 2\n" + "Kristina => 5\n", sb.toString());
    }

    @Test
    public void MainFromHomeworkTest3() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
        while (iter.hasNext()) {
            SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            iter.remove();
        }
        assertEquals(0, examMarks.size());
    }



}
