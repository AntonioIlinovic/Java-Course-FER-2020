package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.ElementsGetter;
import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;

public class ElementsGetterDemo_3 {

    public static void main(String[] args) {
        Collection col;
        if (false)
            col = new ArrayIndexedCollection(); // npr. new ArrayIndexedCollection();
        else
            col = new LinkedListIndexedCollection(); // npr. new LinkedListIndexedCollection();

        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");

        ElementsGetter getter = col.createElementsGetter();

        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
        System.out.println("Jedan element: " + getter.getNextElement());
    }

}
