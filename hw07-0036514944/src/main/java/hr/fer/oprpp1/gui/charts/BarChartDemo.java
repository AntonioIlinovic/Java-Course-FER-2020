package hr.fer.oprpp1.gui.charts;

import hr.fer.oprpp1.gui.layouts.CalcLayout;
import hr.fer.oprpp1.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Razred BarChartDemo koji je izveden iz JFrame i prikazuje preko čitave svoje površine jedan primjerak grafa. Program
 * prima jedan argument: stazu do datoteke u kojoj je opis grafa. Program otvara datoteku, temeljem sadržaja stvara
 * objekt BarChart, njega predaje prozoru koji dalje radi što treba.
 */
public class BarChartDemo extends JFrame {

    private static final long serialVersionUID = 1L;

    private String pathName;
    private BarChart barChart;

    /**
     * Uz gornji vrh prozora stavite jednu labelu u kojoj će biti ispisana (vodoravno centrirano) putanja do datoteke iz
     * koje su podatci dohvaćeni (spomenuta labela je primjerak od JLabel, komponenta tipa BarChartComponent ne
     * prikazuje na sebi na vrhu nikakvu labelu); ove dvije komponente rasporedite u prozoru prikladnim upravljačem
     * razmještaja.
     *
     * @param args putanja do datoteke u kojoj je opis grafa
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1)
            throw new IllegalArgumentException("Main program accepts one argument: path to graph description file.");

        File file = new File(args[0]);
        Scanner sc = new Scanner(file);

        String xDescription = sc.nextLine();
        String yDescription = sc.nextLine();
        List<XYValue> xyValueList = Arrays.stream(sc.nextLine()
                .split(" "))
                .map(point -> {
                    String[] coordinates = point.split(",");
                    return new XYValue(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                })
                .collect(Collectors.toList());
        int yMin = Integer.parseInt(sc.nextLine());
        int yMax = Integer.parseInt(sc.nextLine());
        int yDistance = Integer.parseInt(sc.nextLine());

        BarChart barChart = new BarChart(xyValueList, xDescription, yDescription, yMin, yMax, yDistance);

        SwingUtilities.invokeLater(() -> {
            new BarChartDemo(file.getAbsolutePath(), barChart).setVisible(true);
        });
    }

    public BarChartDemo(String pathName, BarChart barChart) {
        super();
        this.pathName = pathName;
        this.barChart = barChart;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bar Chart");
        setLocation(-600, 100);
        setSize(500, 500);
        initGUI();
        pack();
    }

    public void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(pathName);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        label.setOpaque(true);
        cp.add(label);

        BarChartComponent barChartComponent = new BarChartComponent(barChart);
        barChartComponent.setBackground(Color.RED);
        // TODO Add painting with border
        //barChartComponent.setBorder(BorderFactory.createLineBorder(Color.RED, 40));
        barChartComponent.setOpaque(true);
        cp.add(barChartComponent);
    }

}
