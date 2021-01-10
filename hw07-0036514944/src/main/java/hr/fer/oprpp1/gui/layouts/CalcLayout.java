package hr.fer.oprpp1.gui.layouts;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Custom made layout manager CalcLayout which implements LayoutManager2. Limitations with which it will work are
 * instances of RCPosition class which offers two read-only attributes: row and column (both int). Class offers public
 * static factory method (RCPosition parse(text)) which accepts textual specification (such as "3,7") and returns
 * appropriate RCPosition object. For component arrangement layout manager conceptually works with grid of 5 rows and 7
 * columns (this is fixed, and unavailable for change). Numeration of rows and columns starts from 1. Appearance of
 * arrangement are shown (with coordinates written):
 * </p>
 * <p>
 * -----------------------------                                                                                       .
 * |        1,1        |1,6|1,7|                                                                                       .
 * |---------------------------|                                                                                       .
 * |2,1|2,2|2,3|2,4|2,5|2,6|2,7|                                                                                       .
 * |---------------------------|                                                                                       .
 * |3,1|3,2|3,3|3,4|3,5|3,6|3,7|                                                                                       .
 * |---------------------------|                                                                                       .
 * |4,1|4,2|4,3|4,4|4,5|4,6|4,7|                                                                                       .
 * |---------------------------|                                                                                       .
 * |5,1|5,2|5,3|5,4|5,5|5,6|5,7|                                                                                       .
 * |---------------------------|
 * <p>
 * Note: while calculating arrangement (on which x starts all of "virtual" columns and how wide it is, same for rows)
 * use decimal numbers and round them accordingly. Otherwise this will happen: while gradually expansion of the window
 * for some time nothing will change but empty space beside right and lower border of the window will increase, then
 * suddenly new arrangement will be shown. That is not the desirable behaviour.
 * </p>
 * <p>
 * Not all columns and rows need to be equally wide. For example if width of the window is 200, and there are 7 columns,
 * width of one column would be 200/7=28.57...; If we round this, we get 29, but 29*7=203, and we don't have that much
 * space. That's why some columns will be 29 wide, and some 28 wide, and these inequalities should be uniformly
 * distributed through space (In this case 29+28+29+28+29+28+29, and not 29+20+20+20+29+28+28).
 * </p>
 * <p>
 * Adding components on coordinates (1,2) to (1,5) should throw an exception (and each other non legal position, for
 * example (-2,0) or (1,8). Exception should also be thrown if there is an attempt to add a component where there is
 * already a component. That exception is named CalcLayoutException and is a subclass of RuntimeException.
 * </p>
 * <p>
 * During calculation of preferred dimensions of arrangements hold to these assumptions. Height of all rows is the same
 * and is determined by maximum height of preferred heights of all added components. Width of all columns is the same
 * and is determined as maximum width off all preferred widths of all components (excluding that one on position (1,1) -
 * watch out how that number is interpreted). Components might not be of exactly those dimensions, so you need to change
 * their width and height according to previously given instructions.
 * </p>
 * <p>
 * It si legal that not all components are present. Whole rows or columns can be empty, and that is legal.
 * </p>
 * <p>
 * CalcLayout has two constructors: one constructor with one number (wanted distance between rows adn columns (in
 * pixels; int type)) and other constructor without arguments which puts that wanted distance property to 0.
 * </p>
 */
public class CalcLayout implements LayoutManager2 {

    /* Gap between components in CalcLayout, vertical and horizontal */
    private int componentGap;
    /* All Components added to this CalcLayout are stored in this map */
    private Map<RCPosition, Component> addedComponents;

    /**
     * Constructor with one parameter, wanted gap between all components.
     *
     * @param componentGap gap between components
     */
    public CalcLayout(int componentGap) {
        this.componentGap = componentGap;
        this.addedComponents = new HashMap<>();
    }

    /**
     * Constructor without parameters, gap between all components is set to 0.
     */
    public CalcLayout() {
        this(0);
    }


    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Method addLayoutComponent(String name, Component comp) is not supported!");
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        addedComponents.values().remove(comp);
    }

    private interface GetLayoutMethod {
        double getWidth(Component comp);
        double getHeight(Component comp);
    }

    private Dimension getLayoutSize(Container parent, Map<RCPosition, Component> componentMap, GetLayoutMethod getLayoutMethod) {
        Insets ins = parent.getInsets();

        double maxCompWidth = componentMap.entrySet()
                .stream()
                .filter(rcPositionComponentEntry ->
                        !rcPositionComponentEntry.getKey().equals(new RCPosition(1, 1)))
                .map(Map.Entry::getValue)
                .mapToDouble(getLayoutMethod::getWidth)
                .max()
                .orElse(0.0);
        // Interpret component at position (1,1) differently
        Component componentAt_1_1 = componentMap.get(new RCPosition(1, 1));
        maxCompWidth = Math.max(
                maxCompWidth,
                // Check if component at (1,1) even exists
                componentAt_1_1 == null ? 0.0 : (getLayoutMethod.getWidth(componentAt_1_1) - 4 * componentGap) / 5);

        double maxCompHeight = componentMap.values()
                .stream()
                .mapToDouble(getLayoutMethod::getHeight)
                .max()
                .orElse(0.0);

        double preferredWidth = 7 * maxCompWidth + 6 * componentGap + ins.left + ins.right;
        double preferredHeight = 5 * maxCompHeight + 4 * componentGap + ins.top + ins.bottom;

        return new Dimension((int) preferredWidth, (int) preferredHeight);
    }


    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(parent, addedComponents,
                new GetLayoutMethod() {
                    @Override
                    public double getWidth(Component comp) {
                        return comp.getPreferredSize().width;
                    }

                    @Override
                    public double getHeight(Component comp) {
                        return comp.getPreferredSize().height;
                    }
                }
        );
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent, addedComponents,
                new GetLayoutMethod() {
                    @Override
                    public double getWidth(Component comp) {
                        return comp.getMinimumSize().width;
                    }

                    @Override
                    public double getHeight(Component comp) {
                        return comp.getMinimumSize().height;
                    }
                }
        );
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension dim = parent.getSize();
        Insets ins = parent.getInsets();

        Map<RCPosition, Rectangle> posBoundsMap = WidthHeightDistributor.getPositionRectangleMap(dim, ins, componentGap);

        addedComponents.forEach((rcPosition, component) -> {
            Rectangle r = posBoundsMap.get(rcPosition);
            component.setBounds(r.x, r.y, r.width, r.height);
        });

    }

    /**
     * Inner static class with one public method getPositionRectangleMap which will return map with keys of RCPosition
     * and values of Rectangle. It is HARDCODED appropriate to CalcLayout (5 rows, 7 columns). User needs to enter
     * parent Dimension, Insets and componentGap from CalcLayout to get valid map of Rectangles.
     */
    private static class WidthHeightDistributor {
        private final Map<RCPosition, Rectangle> positionRectangleMap;

        private Dimension dim;
        private Insets ins;
        private int componentGap;

        /**
         * will return map with keys of RCPosition and values of Rectangle. It is HARDCODED appropriate to CalcLayout (5
         * rows, 7 columns). User needs to enter parent Dimension, Insets and componentGap from CalcLayout to get valid
         * map of Rectangles. Values of Rectangles represent Bounds in parent container, meaning (rectangle.x -> x from
         * which parent Container starts drawing a Component. rectangle.y -> y from which parent Container starts
         * drawing a Component. rectangle.width -> width of a Component that parent Container draws. rectangle.height ->
         * height of a Component that parent Container draws.
         *
         * @param dim          Dimension of parent container
         * @param ins          Insets of parent container
         * @param componentGap gap between components in parent container
         * @return map representing Bounds of components in parent container at specific RCPosition
         */
        public static Map<RCPosition, Rectangle> getPositionRectangleMap(Dimension dim, Insets ins, int componentGap) {
            WidthHeightDistributor widthHeightDistributor = new WidthHeightDistributor(dim, ins, componentGap);
            return widthHeightDistributor.positionRectangleMap;
        }

        private WidthHeightDistributor(Dimension dim, Insets ins, int componentGap) {
            this.dim = dim;
            this.ins = ins;
            this.componentGap = componentGap;
            positionRectangleMap = new HashMap<>();

            /* Calculate widths of each component as integer. Algorithm goes like this for example when we need to
            distribute 500 pixels to 7 components:

            1 * 500 / 7 = 71.4286
            2 * 500 / 7 = 142.8571
            3 * 500 / 7 = 214.2857
            4 * 500 / 7 = 285.7143
            5 * 500 / 7 = 357.1429
            6 * 500 / 7 = 428.5714
            7 * 500 / 7 = 500.0000

            1st comp will take 71 pixels
            2nd comp will take 143 - (sum of all that previous comp have taken) = 143 - 71 = 72
            3rd comp will take 214 - (sum of all that previous comp have taken) = 143 - 71 - 72 = 71
            4th comp will take 286 - (sum of all that previous comp have taken) = 143 - 71 - 72 - 71 = 71
            ...
             */
            int widths[] = new int[7];
            int widthUsedByComponents = dim.width - 6 * componentGap - ins.left - ins.right;
            double widthPerComponentDouble = widthUsedByComponents / 7.0;
            int widthAdderInt = 0;
            double widthAdderDouble = 0.0;
            for (int comp = 0; comp < 7; comp++) {
                widthAdderDouble += widthPerComponentDouble;
                int rounded = (int) Math.round(widthAdderDouble);
                widths[comp] = rounded - widthAdderInt;
                widthAdderInt += widths[comp];
            }

            int heights[] = new int[5];
            int heightUsedByComponents = dim.height - 4 * componentGap - ins.top - ins.bottom;
            double heightPerComponentDouble = heightUsedByComponents / 5.0;
            int heightAdderInt = 0;
            double heightAdderDouble = 0.0;
            for (int comp = 0; comp < 5; comp++) {
                heightAdderDouble += heightPerComponentDouble;
                int rounded = (int) Math.round(heightAdderDouble);
                heights[comp] = rounded - heightAdderInt;
                heightAdderInt += heights[comp];
            }


            /* Sum widths and heights to position at index. For example:
             widthsSummedToPosition[3] = widths[0] + widths[1] + widths[2]
             widthsSummedToPosition[4] = widths[0] + widths[1] + widths[2] + widths[3]
             ...
             */
            List<Integer> widthsSummedToPosition = new ArrayList<>();
            List<Integer> heightsSummedToPosition = new ArrayList<>();
            for (int i = 0, sum = 0; i < 7; i++) {
                widthsSummedToPosition.add(i, widths[i] + sum);
                sum += widths[i];
            }
            for (int i = 0, sum = 0; i < 5; i++) {
                heightsSummedToPosition.add(i, heights[i] + sum);
                sum += heights[i];
            }
            // We increase the size of summedToPositions by 1 because we will later use its value at index 0 as value 0
            widthsSummedToPosition.add(0, 0);
            heightsSummedToPosition.add(0, 0);


            /* Fill map of RCPositions and appropriate Rectangle for Component Bounds */
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 7; col++) {
                    // Skip adding to positions at first row and columns from second to fifth
                    if (row == 0 && (col >= 1 && col <= 4))
                        continue;
                    // Add component at (1,1) differently
                    if (row == 0 && col == 0) {
                        positionRectangleMap.put(new RCPosition(1, 1), new Rectangle(
                                ins.left, ins.top, widthsSummedToPosition.get(5) + 4 * componentGap, heightsSummedToPosition.get(1)
                        ));
                        continue;
                    }

                    // Add components to map
                    positionRectangleMap.put(new RCPosition(row + 1, col + 1), new Rectangle(
                            ins.left + col * componentGap + widthsSummedToPosition.get(col),
                            ins.top + row * componentGap + heightsSummedToPosition.get(row),
                            widths[col],
                            heights[row]
                    ));
                }
            }
        }
    }


    // Methods from LayoutManager2

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (comp == null || constraints == null)
            throw new NullPointerException("Component or Constraint can't be null!");
        if (!(constraints instanceof RCPosition || constraints instanceof String))
            throw new IllegalArgumentException("Constraint of component must be instance of type String or RCPosition, yours was " + constraints.getClass());

        RCPosition rcPosition;
        /* Initialize RCPosition by given Object constraint (it can be in form of a String or just another RCPosition) */
        if (constraints instanceof String) {
            String stringConstraints = (String) constraints;
            try {
                rcPosition = RCPosition.parse(stringConstraints);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(stringConstraints + " can't be parsed to RCPosition!");
            }
        } else {
            rcPosition = (RCPosition) constraints;
        }

        int row = rcPosition.getRow();
        int col = rcPosition.getCol();
        if (row < 1 || row > 5 || col < 1 || col > 7 || (row == 1 && (col >= 2 && col <= 5)))
            throw new CalcLayoutException("Invalid row and column value of component: row=(" + row + "), column=(" + col + ")");
        if (addedComponents.containsKey(rcPosition))
            throw new CalcLayoutException("Component already added on that position: row=(" + row + "), column=(" + col + ")");

        /* If given constraint is valid for CalcLayout add it to map of added components */
        addedComponents.put(rcPosition, comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        return getLayoutSize(parent, addedComponents,
                new GetLayoutMethod() {
                    @Override
                    public double getWidth(Component comp) {
                        return comp.getMaximumSize().width;
                    }

                    @Override
                    public double getHeight(Component comp) {
                        return comp.getMaximumSize().height;
                    }
                }
        );
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
        // Empty implementation
    }

}
