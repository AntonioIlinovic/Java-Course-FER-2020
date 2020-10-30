package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

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
}
