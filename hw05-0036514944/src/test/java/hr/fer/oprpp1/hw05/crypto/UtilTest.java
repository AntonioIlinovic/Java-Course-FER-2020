package hr.fer.oprpp1.hw05.crypto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {

    @Test
    public void testHextobyteMethod() {
        byte[] expected = new byte[] {1, -82, 34};
        byte[] calculated = Util.hextobyte("01aE22");
        assertArrayEquals(expected, calculated);

        assertThrows(IllegalArgumentException.class, () -> {
            Util.hextobyte("01a");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Util.hextobyte("01aG");
        });
    }

    @Test
    public void testBytetohexMethod() {
        String expected = "01ae22";
        String calculated = Util.bytetohex(new byte[] {1, -82, 34});
        assertEquals(expected, calculated);

        assertEquals("", Util.bytetohex(new byte[] {}));
    }

}
