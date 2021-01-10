package hr.fer.oprpp1.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.sqrt;

public class PrimDemo extends JFrame {

    private static final long serialVersionUID = 1L;

    public PrimDemo() {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Prim Brojevi");
        setLocation(200, 200);
        setSize(500, 500);
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        PrimModelList primModelList = new PrimModelList();

        JList<Integer> list1 = new JList<>(primModelList);
        JList<Integer> list2 = new JList<>(primModelList);

        JPanel p = new JPanel(new GridLayout(1, 0));
        p.add(new JScrollPane(list1));
        p.add(new JScrollPane(list2));

        cp.add(p, BorderLayout.CENTER);

        JButton addPrim = new JButton("Dodaj slijedeći prim broj!");
        cp.add(addPrim, BorderLayout.PAGE_END);

        addPrim.addActionListener(e -> {
            primModelList.next();
        });
    }

    /**
     * Model which stores prime numbers. It has method next() which will generate next prime number and notify its
     * observers.
     */
    private static class PrimModelList implements ListModel<Integer> {

        private final List<ListDataListener> observers = new ArrayList<>();
        private final List<Integer> primNumbers = new ArrayList<>(Collections.singletonList(2));

        /**
         * Model adds to stored prim numbers next prim number and notifies the observers.
         */
        public void next() {
            /* Add next prim number after the last stored prim number */
            int lastStoredPrim = primNumbers.get(primNumbers.size() - 1);
            int nextPrimNumber = nextPrime(lastStoredPrim);
            primNumbers.add(nextPrimNumber);

            /* Interval added after one number was added */
            int lastIndex = primNumbers.size() - 1;
            ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, lastIndex, lastIndex);
            for (ListDataListener l : observers)
                l.intervalAdded(e);
        }

        /**
         * Calculates next prim number after the given input.
         *
         * @param input from which it starts calculating next prime number
         * @return next prime number greater than the given argument
         */
        private int nextPrime(int input) {
            int counter;
            input++;
            while (true) {
                int l = (int) sqrt(input);
                counter = 0;
                for (int i = 2; i <= l; i++) {
                    if (input % i == 0) counter++;
                }
                if (counter == 0)
                    return input;
                else {
                    input++;
                    continue;
                }
            }
        }

        @Override
        public int getSize() {
            return primNumbers.size();
        }

        @Override
        public Integer getElementAt(int index) {
            return primNumbers.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            observers.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            observers.remove(l);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrimDemo().setVisible(true);
        });
    }

}
