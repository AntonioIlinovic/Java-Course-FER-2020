package hr.fer.oprpp1.gui.charts;

import java.util.List;

/**
 * razred BarChart.
 */
public class BarChart {

    /* Vrijednosti BarChart-a kao (x,y) koordinate */
    private List<XYValue> xyValueList;
    /* Opis kraj x osi */
    private String xDescription;
    /* Opis kraj y osi */
    private String yDescription;
    /* Minimalan y od kojeg BarChart prikazuje svoje vrijednosti */
    private int yMin;
    /* Maksimalan y do kojeg BarChart prikazuje svoje vrijednosti */
    private int yMax;
    /* Brojčana razlika u kojim koracima se brojevi na y osi prikazuju */
    private int yGap;

    /**
     * kroz konstruktor prima listu XYValue objekata, opis uz x-os te opis uz y-os, minimalni y koji se prikazuje na osi
     * (baciti iznimku ako je negativan), maksimalni y koji se prikazuje na osi (baciti iznimku ako nije strogo veći od
     * predanog minimuma) te razmak između dva susjedna y-a koji se prikazuju na osi (ako ymax-ymin ne dijeli taj
     * razmak, pri crtanju radite s prvim većim y-om koji je na cijelom broju razmaka od ymin).
     */
    public BarChart(List<XYValue> xyValueList, String xDescription, String yDescription, int yMin, int yMax, int yGap) {
        if (yMin < 0)
            throw new IllegalArgumentException("y minimum can't be negative!");
        if (yMax < 0)
            throw new IllegalArgumentException("y maximum can't be negative!");
        /*
         Konstruktor od BarChart treba napraviti provjeru jesu li y-vrijednosti u svim podatcima predanim u listi
         veće ili jednake od predanog ymin pa ako postoji  neka koja je manja, treba baciti iznimku.
         Ne radi se provjera za maksimume.
         */
        xyValueList.stream()
                .mapToDouble(XYValue::getY)
                .filter(value -> value < yMin)
                .findAny()
                .ifPresent(value -> {
                    throw new IllegalArgumentException("All XYValue objects must have y value greater or equal yMinimum (" + yMin + ")!");
                });

        // TODO razmak između dva susjedna y-a koji se prikazuju na osi (ako ymax-ymin ne dijeli taj
        //      razmak, pri crtanju radite s prvim većim y-om koji je na cijelom broju razmaka od ymin).

        this.xyValueList = xyValueList;
        this.xDescription = xDescription;
        this.yDescription = yDescription;
        this.yMin = yMin;
        this.yMax = yMax;

        /* razmak između dva susjedna y-a koji se prikazuju na osi (ako ymax-ymin ne dijeli taj razmak,
         pri crtanju radite s prvim većim y-om koji je na cijelom broju razmaka od ymin). */
        int gapMaxMin = yMax - yMin;
        if (yGap > gapMaxMin)
            this.yGap = gapMaxMin;
        else {
            while (gapMaxMin % yGap != 0)
                yGap++;

            this.yGap = yGap;
        }
    }

    public List<XYValue> getXyValueList() {
        return xyValueList;
    }

    public String getxDescription() {
        return xDescription;
    }

    public String getyDescription() {
        return yDescription;
    }

    public int getyMin() {
        return yMin;
    }

    public int getyMax() {
        return yMax;
    }

    public int getyGap() {
        return yGap;
    }
}
