package hr.fer.oprpp1.gui.layouts;

import hr.fer.oprpp1.gui.layouts.demo.DemoFrame1;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalcLayoutTest {

    private JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        return l;
    }

    @Test
    public void invalidRowOrColumnException() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Container cp = jFrame.getContentPane();
        cp.setLayout(new CalcLayout(5));

        // Throw exception if component is added in (row, column), (r,c) where r<1 || r>5 or c<1 || c>7
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(0,2)));
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(6,2)));
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(2,0)));
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(2,8)));

        // Throw exception if component is added in (1, s) where s>1 && s<6
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(1,2)));
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(1,3)));
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(1,4)));
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 1"), new RCPosition(1,5)));

        // Throw exception if multiple component are added on same coordinates
        cp.add(l("tekst 1"), new RCPosition(2,2));
        assertThrows(CalcLayoutException.class, () -> cp.add(l("tekst 2"), new RCPosition(2,2)));
    }

    @Test
    public void componentsAddedScenario1Test() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    public void componentsAddedScenario2Test() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    public void testWidtAndHeight3() {
        JPanel p = new JPanel(new CalcLayout());
        p.setSize(200, 500);
        p.add(new JLabel("dodan"), new RCPosition(1, 1));
        p.add(new JLabel("dodan2"), new RCPosition(1, 6));
        p.add(new JLabel("dodan3"), new RCPosition(4, 6));
        p.add(new JLabel("dodan"), new RCPosition(5, 1));
        p.add(new JLabel("dodan"), new RCPosition(5, 4));
        p.doLayout();

        int width1 = p.getComponent(0).getSize().width;
        assertEquals(29 + 28 + 29 + 28 + 29, width1);

        int width2 = p.getComponent(1).getSize().width;
        assertEquals(28, width2);

        int width3 = p.getComponent(2).getSize().width;
        assertEquals(28, width3);

        int width4 = p.getComponent(3).getSize().width;
        assertEquals(29, width4);

        int width5 = p.getComponent(4).getSize().width;
        assertEquals(28, width5);
    }

}
