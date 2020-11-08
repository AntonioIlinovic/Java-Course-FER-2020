package hr.fer.oprpp1.hw04.db;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class which will print given records in wanted format:
 *
 * example_1_start --------------------
 * +============+========+========+===+
 * | 0000000003 | Bosnić | Andrea | 4 |
 * +============+========+========+===+
 * Records selected: 1
 *
 * example_1_end ----------------------
 * example_2_start --------------------
 * +============+===========+===========+===+
 * | 0000000002 | Bakamović | Petra | 3 |
 * | 0000000003 | Bosnić | Andrea | 4 |
 * | 0000000004 | Božić | Marin | 5 |
 * | 0000000005 | Brezović | Jusufadis | 2 |
 * +============+===========+===========+===+
 * Records selected: 4
 *
 * example_2_end ----------------------
 * example_3_start --------------------
 * Records selected: 0
 *
 * example_3_end
 */
public class RecordFormatter {

    /**
     * Method which returns List of Strings so it can be printed in wanted format.
     * @param records to print
     * @return List of Strings in wanted format
     */
    public static List<String> format(List<StudentRecord> records) {
        List<String> formattedLines = new ArrayList<>();
        if (records.size() == 0) {
            formattedLines.add(recordsSelected(0));
            return formattedLines;
        }

        int maxJmbagLength = maxAttributeLength(records, FieldValueGetters.JMBAG).orElse(0);
        int maxFirstNameLength = maxAttributeLength(records, FieldValueGetters.FIRST_NAME).orElse(0);
        int maxLastNameLength = maxAttributeLength(records, FieldValueGetters.LAST_NAME).orElse(0);

        formattedLines.add(getDelimiter(maxJmbagLength, maxFirstNameLength, maxLastNameLength));
        records.forEach(r -> formattedLines.add(getFormattedRecord(r, maxJmbagLength, maxFirstNameLength, maxLastNameLength)));
        formattedLines.add(getDelimiter(maxJmbagLength, maxFirstNameLength, maxLastNameLength));
        formattedLines.add(recordsSelected(records.size()));

        return formattedLines;
    }

    /**
     * Helper method which return StudentRecord in format that matches given max lengths.
     * example: "| 0000000003 | Bosnić | Andrea | 4 |"
     * @param studentRecord length
     * @param maxJmbagLength length
     * @param maxFisrtNameLength length
     * @param maxLastNameLength length
     * @return StudentRecord in format: "| 0000000003 | Bosnić | Andrea | 4 |"
     */
    private static String getFormattedRecord(StudentRecord studentRecord, int maxJmbagLength, int maxFisrtNameLength, int maxLastNameLength) {
        return "| " +
                padToFixedLength(studentRecord.getJmbag(), maxJmbagLength) +
                " | " +
                padToFixedLength(studentRecord.getLastName(), maxLastNameLength) +
                " | " +
                padToFixedLength(studentRecord.getFirstName(), maxFisrtNameLength) +
                " | " +
                studentRecord.getFinalGrade() +
                " |";
    }

    /**
     * Helper method which pads given String with whitespaces to length.
     * @param input String to pad with whitespaces
     * @param length pad with this many whitespaces
     * @return padded input String
     */
    private static String padToFixedLength(String input, int length) {
        return String.format("%-" + length + "s", input);
    }

    /**
     * Helper method which returns delimiter in format: +=====+=====+====+===+
     * @param maxJmbagLength length
     * @param maxFisrtNameLength length
     * @param maxLastNameLength length
     * @return delimiter in format: +=====+=====+====+===+
     */
    private static String getDelimiter(int maxJmbagLength, int maxFisrtNameLength, int maxLastNameLength) {
        return "+=" +
                getStringOfNStrings(maxJmbagLength, "=") +
                "=+=" +
                getStringOfNStrings(maxLastNameLength, "=") +
                "=+=" +
                getStringOfNStrings(maxFisrtNameLength, "=") +
                "=+===+";
    }

    /**
     * Helper method which returns String composed of n given String.
     * @param n number of given Strings
     * @param replacedString given String
     * @return String composed of n given Strings
     */
    private static String getStringOfNStrings(int n, String replacedString) {
        return IntStream.range(0, n)
                .mapToObj(i -> replacedString)
                .collect(Collectors.joining(""));
    }

    /**
     * Helper method which returns how many records was selected as a String.
     * @param n number of selected records
     * @return records selected as String
     */
    private static String recordsSelected(int n) {
        return "Records selected: " + n + "\n";
    }

    /**
     * Helper method which returns max length of given records of given field.
     * @param records of students
     * @param valueGetter of what max length is requested
     * @return max length of given records given field
     */
    private static OptionalInt maxAttributeLength(List<StudentRecord> records, IFieldValueGetter valueGetter) {
        return records.stream()
                .mapToInt(r -> valueGetter.get(r).length())
                .max();
    }

}
