package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;
import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Processor;

import java.util.Arrays;

public class MyDemo {

    public static void main(String[] args) {
        ArrayIndexedCollection col = new ArrayIndexedCollection(2);
        col.add(Integer.valueOf(20));
        col.add("New York");
        col.add("San Francisco"); // Here the internal array is reallocated to 4
        System.out.println(col.contains("New York")); // Writes: true
        col.remove(1); // Removes "New York"; shifts "San Francisco" to position 1
        System.out.println(col.get(1)); // Writes: "San Francisco
        System.out.println(col.size()); // Writes: 2
        col.add("Los Angeles");

        // This is local class representing a Processor which writes objects to System.out
        class P implements Processor {
            public void process(Object o) {
                System.out.println(o);
            }
        }

        System.out.println("\ncol elements:");
        col.forEach(new P());

        LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col);
        System.out.println("\ncol2 elements:");
        col2.forEach(new P());

        LinkedListIndexedCollection col3 = new LinkedListIndexedCollection(col2);
        System.out.println("\ncol3 elements:");
        col3.forEach(new P());

        System.out.println("\ncol elements again:");
        System.out.println(Arrays.toString(col.toArray()));

        System.out.println("\ncol2 elements again:");
        System.out.println(Arrays.toString(col2.toArray()));

        System.out.println(col.contains(col2.get(1))); // true
        System.out.println(col2.contains(col.get(1))); // true

        col.remove(Integer.valueOf(20)); // Removes 20 from collection (at position 0).
        col.remove("NOT INT");
        col2.remove(Integer.valueOf(20));
        col2.remove("NOT INT");

        ArrayIndexedCollection arrayTestRemove = new ArrayIndexedCollection(2);
        arrayTestRemove.add(0);
        arrayTestRemove.add(1);
    }

}
