package hr.fer.oprpp1.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * <p>
 * Razred izveden iz razreda JComponent. Komponenta na svojoj površini stvara prikaz podataka koji su definirani
 * primljenim objektom (tj. crta stupčasti dijagram). Pri izradi crteža, vodite se sljedećim zahtjevima.
 * </p>
 * <ul>
 *     <li>
 *          Komponenta s lijeve strane ispisuje naziv podataka s y-osi, slijedi neki (fiksni) razmak, vrijednosti
 *          po y-osi koje su desno poravnate (kao na slici), neki fiksni razmak, pa y-os
 *      </li>
 *      <li>
 *          Komponenta s donje strane (gledano od samog dna) ispisuje naziv podataka s x-osi, slijedi neki
 *          (fiksni) razmak, vrijednosti po x-osi koje su horizontalno centrirane ispod stupića (kao na slici), neki
 *          fiksni razmak, pa x-os
 *      </li>
 *      <li>
 *          Za dio osi x i y koji su na kraju (gdje je strelica) uzmite također neki fiksni iznos (ili ga prilagodite
 *          po potrebi; tu imate slobodu).
 *      </li>
 * </ul>
 * <p>
 *     Posljedica ova tri zahtjeva je sljedeća: kada se komponenta razvlači, udaljenost osi y od lijevog ruba te osi x
 *     od dna ostaje fiksna: mijenja se samo visina y-osi, širina x-osi te sam prostor u kojem se prikazuju stupići
 *     (čime i stupići postaju širi/uži, viši/niži). Slika uvijek mora popunjavati cjelokupnu površinu komponente.
 *     Pri razvlačenju komponente nemojte ništa mijenjati oko fontova: font (i veličina fonta) neka uvijek bude ista.
 * </p>
 * <p>
 *     Ne smijete unaprijed ništa pretpostavljati o broju znamenaka vrijednosti koje se ispisuju na y-osi (osim da je
 *     sve prikazivo/zapisivo tipom int).Udaljenost osi y od lijevog ruba mora "plesati" ovisno o stvarno
 *     potrebnom prostoru za prikaz brojeva. Rješenje koje unaprijed alocira fiksan broj (npr. 300 piksela, za svaki
 *     slučaj) u nadi da će svi brojevi biti prikazivi nije prihvatljivo.
 * </p>
 */
public class BarChartComponent extends JComponent {

    private static final long serialVersionUID = 1L;

    /**
     * @param barChart GUI komponenta na kojoj će se iscrtavati BarChart
     */
    public BarChartComponent(BarChart barChart) {
        super();
        this.barChart = barChart;
        setPreferredSize(new Dimension(500, 500));
    }

    BarChart barChart;

    // Fixed widths of borders
    private final int BOTTOM_FIXED = 10;
    private final int LEFT_FIXED = 10;
    private final int TOP_FIXED = 10;
    private final int RIGHT_FIXED = 10;
    private final int NUMBER_AXIS_DISTANCE = 10;
    private final int DESCRIPTION_NUMBER_DISTANCE = 10;
    private int Y_AXIS_NUMBER_WIDTH;
    private int FONT_HEIGHT;

    // Extract values when entering paintComponent method
    private Graphics g;
    private Graphics2D g2d;
    private Insets ins;
    private Dimension dim;
    private FontMetrics fontMetrics;
    private String descriptionX;
    private String descriptionY;
    private int numOfHorizontalGridlines;
    private int numOfVerticalGridlines;

    /* 4 points that represent corners of a BarChart grid */
    private Point LOWER_LEFT_GRID;
    private Point LOWER_RIGHT_GRID;
    private Point UPPER_LEFT_GRID;
    private Point UPPER_RIGHT_GRID;


    @Override
    protected void paintComponent(Graphics g) {
        refreshVariables(g);

        paintDescriptionX();
        paintDescriptionY();

        paintGrid();
        paintAxes();

        paintAxesNumbers();
        paintXYValues();
        //paintPercentageGrid();
    }

    /**
     * Refreshes all variables stored in this class that depend on parent container Dimension, Insets...
     *
     * @param g Graphics object passed from paintComponent method
     */
    private void refreshVariables(Graphics g) {
        Y_AXIS_NUMBER_WIDTH = getFontMetrics(getFont()).stringWidth(String.valueOf(barChart.getyMax()));
        FONT_HEIGHT = getFontMetrics(getFont()).getHeight();

        this.g = g;
        g2d = (Graphics2D) g;
        ins = getInsets();
        dim = getSize();
        fontMetrics = getFontMetrics(getFont());
        descriptionX = barChart.getxDescription();
        descriptionY = barChart.getyDescription();
        numOfVerticalGridlines = barChart.getXyValueList().size();
        numOfHorizontalGridlines = (barChart.getyMax() - barChart.getyMin()) / barChart.getyGap();

        LOWER_LEFT_GRID = new Point(
                LEFT_FIXED + FONT_HEIGHT + DESCRIPTION_NUMBER_DISTANCE + Y_AXIS_NUMBER_WIDTH + NUMBER_AXIS_DISTANCE,
                dim.height - BOTTOM_FIXED - FONT_HEIGHT - DESCRIPTION_NUMBER_DISTANCE - FONT_HEIGHT - NUMBER_AXIS_DISTANCE);
        LOWER_RIGHT_GRID = new Point(
                dim.width - RIGHT_FIXED,
                dim.height - BOTTOM_FIXED - FONT_HEIGHT - DESCRIPTION_NUMBER_DISTANCE - FONT_HEIGHT - NUMBER_AXIS_DISTANCE);
        UPPER_LEFT_GRID = new Point(
                LEFT_FIXED + FONT_HEIGHT + DESCRIPTION_NUMBER_DISTANCE + Y_AXIS_NUMBER_WIDTH + NUMBER_AXIS_DISTANCE,
                TOP_FIXED);
        UPPER_RIGHT_GRID = new Point(
                dim.width - RIGHT_FIXED,
                TOP_FIXED);
    }

    /**
     * Paints numbers besides x and y axes.
     */
    private void paintAxesNumbers() {
        g2d.setColor(Color.BLACK);

        /* height and width percentage represent how much of grid does one grid block occupy */
        double heightPercentage = 1.0 / ((double) (barChart.getyMax() - barChart.getyMin()) / barChart.getyGap());
        double widthPercentage = 1.0 / barChart.getXyValueList().size();
        /* block height and width represents how much pixels does one block of grid occupy */
        double blockHeight = heightPercentage * (LOWER_LEFT_GRID.y - UPPER_LEFT_GRID.y);
        double blockWidth = widthPercentage * (LOWER_RIGHT_GRID.x - LOWER_LEFT_GRID.x);

        /* X axis numbers */
        for (int i = 1; i <= barChart.getXyValueList().size(); i++) {
            String drawString = String.valueOf(i);

            g2d.drawString(drawString,
                    (float) (LOWER_LEFT_GRID.x + ((i - 0.5) * blockWidth) - fontMetrics.stringWidth(drawString) / 2),
                    LOWER_LEFT_GRID.y + NUMBER_AXIS_DISTANCE + fontMetrics.getHeight());
        }

        /* Y axis numbers */
        for (int numToDraw = barChart.getyMin(), iter = 0; numToDraw <= barChart.getyMax(); numToDraw += barChart.getyGap(), iter++) {
            String drawString = String.valueOf(numToDraw);

            g2d.drawString(drawString,
                    LOWER_LEFT_GRID.x - NUMBER_AXIS_DISTANCE - fontMetrics.stringWidth(drawString),
                    (float) (LOWER_LEFT_GRID.y - iter * blockHeight) + fontMetrics.getDescent());
        }

        g2d.setColor(getForeground());
    }

    /**
     * Paints BarChart's values in coordinate system depending on its values.
     */
    private void paintXYValues() {
        g2d.setColor(Color.ORANGE);

        // Draw each XYValue on the graph as an Rectangle
        for (int i = 0; i < barChart.getXyValueList().size(); i++) {
            XYValue xyValue = barChart.getXyValueList().get(i);

            /* height and width percentage represent how much of grid does current xyValue cover in percents of whole grid */
            double heightPercentage = ((double) xyValue.getY() - barChart.getyMin()) / (barChart.getyMax() - barChart.getyMin());
            double widthPercentage = 1.0 / barChart.getXyValueList().size();

            /*  +1, -2 are inside this function because we first draw gridlines and after that we fill XYValues,
            if we didn't change expression with these 2 numbers, rectangles wouldn't be separated.
             */
            g2d.fillRect(
                    (int) (LOWER_LEFT_GRID.x + 1 + i * widthPercentage * (UPPER_RIGHT_GRID.x - UPPER_LEFT_GRID.x)),
                    (int) (LOWER_LEFT_GRID.y - heightPercentage * (LOWER_LEFT_GRID.y - UPPER_LEFT_GRID.y) + 1),
                    (int) (widthPercentage * (LOWER_RIGHT_GRID.x - LOWER_LEFT_GRID.x) - 2),
                    (int) (heightPercentage * (LOWER_LEFT_GRID.y - UPPER_LEFT_GRID.y) - 2)
            );
        }

        g2d.setColor(getForeground());
    }

    /**
     * Paints grid of vertical and horizontal line for coordinate system. Number of lines depends on number of values in
     * BarChart and given specification in BarChart for representation of those values in y axis.
     */
    private void paintGrid() {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2f));

        /* Draw horizontal gridlines */
        for (int numHorizontal = 0; numHorizontal <= numOfHorizontalGridlines; numHorizontal++) {
            double percentage = (double) numHorizontal / numOfHorizontalGridlines;
            g2d.drawLine(
                    UPPER_LEFT_GRID.x - 3,
                    UPPER_LEFT_GRID.y + (int) (percentage * (LOWER_LEFT_GRID.y - UPPER_LEFT_GRID.y)),
                    UPPER_RIGHT_GRID.x + 3,
                    UPPER_LEFT_GRID.y + (int) (percentage * (LOWER_LEFT_GRID.y - UPPER_LEFT_GRID.y))
            );
        }

        /* Draw vertical gridlines */
        for (int numVertical = 0; numVertical <= numOfVerticalGridlines; numVertical++) {
            double percentage = (double) numVertical / numOfVerticalGridlines;
            g2d.drawLine(
                    UPPER_LEFT_GRID.x + (int) (percentage * (UPPER_RIGHT_GRID.x - UPPER_LEFT_GRID.x)),
                    UPPER_LEFT_GRID.y - 3,
                    UPPER_LEFT_GRID.x + (int) (percentage * (UPPER_RIGHT_GRID.x - UPPER_LEFT_GRID.x)),
                    LOWER_LEFT_GRID.y + 3
            );
        }

        g2d.setColor(getForeground());
        g2d.setStroke(new BasicStroke());
    }

    /**
     * Paints x and y axes with non-filled arrows at the end.
     */
    private void paintAxes() {
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2f));

        g2d.drawLine(
                LOWER_LEFT_GRID.x,
                LOWER_LEFT_GRID.y,
                LOWER_RIGHT_GRID.x,
                LOWER_RIGHT_GRID.y);
        g2d.drawLine(
                LOWER_LEFT_GRID.x,
                LOWER_LEFT_GRID.y,
                UPPER_LEFT_GRID.x,
                UPPER_LEFT_GRID.y);
        paintArrows();

        g2d.setColor(getForeground());
        g2d.setStroke(new BasicStroke());
    }

    /**
     * Paints non-filled arrows at the end of x and y axes.
     */
    private void paintArrows() {
        g2d.drawPolygon(
                new int[]{UPPER_LEFT_GRID.x - 3, UPPER_LEFT_GRID.x, UPPER_LEFT_GRID.x + 3},
                new int[]{UPPER_LEFT_GRID.y, UPPER_LEFT_GRID.y - 6, UPPER_LEFT_GRID.y},
                3
        );
        g2d.drawPolygon(
                new int[]{LOWER_RIGHT_GRID.x, LOWER_RIGHT_GRID.x + 6, LOWER_RIGHT_GRID.x},
                new int[]{LOWER_RIGHT_GRID.y - 3, LOWER_RIGHT_GRID.y, LOWER_RIGHT_GRID.y + 3},
                3
        );
    }

    /**
     * Helper method which will paint grid of vertical and horizontal lines in steps of 10% of a component. It is used
     * in testing purposes.
     */
    private void paintPercentageGrid() {
        for (double percentage = .1; percentage <= .9; percentage += .1) {
            g2d.drawLine((int) (percentage * dim.width), 0, (int) (percentage * dim.width), dim.height);
        }
        for (double percentage = .1; percentage <= .9; percentage += .1) {
            g2d.drawLine(0, (int) (percentage * dim.height), dim.width, (int) (percentage * dim.height));
        }
    }

    /**
     * Paints description of x axis.
     */
    private void paintDescriptionX() {
        int x = LOWER_LEFT_GRID.x + (LOWER_RIGHT_GRID.x - LOWER_LEFT_GRID.x) / 2 - fontMetrics.stringWidth(descriptionX) / 2;
        int y = dim.height - BOTTOM_FIXED;

        g2d.drawString(descriptionX, x, y);
    }

    /**
     * Paints description of y axis.
     */
    private void paintDescriptionY() {
        AffineTransform defaultAt = g2d.getTransform();
        // rotates the coordinate by 90 degrees counterclockwise
        AffineTransform at = AffineTransform.getQuadrantRotateInstance(3);
        g2d.setTransform(at);

        g2d.drawString(descriptionY,
                // TODO Fix description y "dancing"
                -fontMetrics.stringWidth(descriptionY) - TOP_FIXED - (LOWER_LEFT_GRID.y - UPPER_LEFT_GRID.y) / 2,
                LEFT_FIXED + fontMetrics.getHeight());

        g2d.setTransform(defaultAt);
    }

}
