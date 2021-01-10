package hr.fer.oprpp1.hw05.crypto;

/**
 * Class with two public static methods: hextobyte(keyText) and bytetohex(bytearray). Method hextobyte(keyText) should
 * take hex-encoded String and return appropriate byte[]. If string is not valid (odd-sized, has invalid characters, …)
 * throw an IllegalArgumentException. For zero-length string, method must return zero-length byte array. Method
 * hextobyte must support both uppercase letters and lowercase letters. Method bytetohex should use lowercase letters.
 */
public class Util {

    /**
     * Method hextobyte(keyText) should take hex-encoded String and return appropriate byte[]. If string is not valid
     * (odd-sized, has invalid characters, …) throw an IllegalArgumentException. For zero-length string, method must
     * return zero-length byte array.
     *
     * @param hexString hex-encoded String
     * @return appropriate byte[]
     */
    public static byte[] hextobyte(String hexString) {
        if (hexString.length() % 2 != 0)
            throw new IllegalArgumentException("Hex String is odd-sized");

        hexString = hexString.toLowerCase();
        byte[] result = new byte[hexString.length() / 2];

        for (int i = 0; i < hexString.length(); i += 2) {
            int firstDigit = hexCharToInt(hexString.charAt(i));
            int secondDigit = hexCharToInt(hexString.charAt(i + 1));

            result[i / 2] = (byte) (16 * firstDigit + secondDigit);
        }

        return result;
    }

    private static int hexCharToInt(char hex) {
        if (hex >= '0' && hex <= '9')
            return hex - '0';
        if (hex >= 'a' && hex <= 'f')
            return hex - 'a' + 10;
        if (hex >= 'A' && hex <= 'F')
            return hex - 'A' + 10;

        throw new IllegalArgumentException("Invalid HEX symbol: " + hex);
    }

    /**
     * Method hextobyte accepts array of bytes and converts it to appropriate hex-encoded String. Method supports both
     * uppercase letters and lowercase letters. Method bytetohex uses lowercase letters.
     *
     * @param bytes array of bytes
     * @return appropriate hex-encoded String
     */
    public static String bytetohex(byte[] bytes) {
        if (bytes.length == 0)
            return "";

        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
