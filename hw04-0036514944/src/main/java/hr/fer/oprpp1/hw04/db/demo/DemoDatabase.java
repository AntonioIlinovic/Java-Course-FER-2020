package hr.fer.oprpp1.hw04.db.demo;

import hr.fer.oprpp1.hw04.db.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Demonstration database class. It uses StudentRecords from database.txt. Implemented operations are query (prints out
 * StudentRecords that satisfy the query) and exit (terminates the program).
 */
public class DemoDatabase {

    /* Path to database file. */
    private static final String databaseFilePath = "src/main/resources/database.txt";

    /* Prompt which is printed every time before the user is allowed to input the query. */
    private static final String PROMPT = "> ";
    /* One of the operations user can enter for program to execute. */
    private static final String OPERATION_QUERY = "query";
    /* One of the operations user can enter for program to execute. */
    private static final String OPERATION_EXIT = "exit";

    public static void main(String[] args) {

        StudentDatabase database;
        try {
            List<String> rows = Files.readAllLines(Paths.get(databaseFilePath), StandardCharsets.UTF_8);
            database = new StudentDatabase(rows);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print(PROMPT);
                String operationLine = sc.nextLine().strip(); // We strip the input line because user can enter ("exit  "), which can cause problems.
                boolean exitProgram = executeOperation(operationLine, database);
                if (exitProgram)
                    break;
            }
        }

    }

    /**
     * Helper method which executes one user operation (query or exit).
     *
     * @param operationLine operation that user entered
     * @param database      database of StudentRecords
     * @return boolean which tells if program should be exited
     */
    private static boolean executeOperation(String operationLine, StudentDatabase database) {
        /* Is user operation was "exit", print appropriate message ("Goodbye!") and stop the program. */
        if (operationLine.equalsIgnoreCase(OPERATION_EXIT)) {
            System.out.println("Goodbye!");
            return true;
        }

        /* Only other acceptable operation is "query". */
        /* We insert limit parameter 2 so that given String is split into two parts (etc. ["query", "jmbag=00000""]) */
        String[] operationLineSplit = operationLine.split("\\s+", 2);
        if (!operationLineSplit[0].equalsIgnoreCase(OPERATION_QUERY))
            throw new IllegalArgumentException("Invalid operation " + operationLineSplit[0] + " given");

        QueryParser parser = new QueryParser(operationLineSplit[1]);
        List<StudentRecord> filteredRecords = getFilteredRecords(database, parser);
        List<String> output = RecordFormatter.format(filteredRecords);
        output.forEach(System.out::println);

        return false;
    }

    /**
     * Helper method which removes PROMPT ("> " in this case) from given input.
     *
     * @param input line that Scanner read
     * @return line without the PROMPT
     */
    private static String removePrompt(String input) {
        return input.substring(1);
    }

    /**
     * Helper method which filters StudentRecords from given StudentDatabase based on query in QueryParser.
     *
     * @param studentDatabase all students
     * @param queryParser     query to filter by
     * @return students from database that satisfy given filter
     */
    private static List<StudentRecord> getFilteredRecords(StudentDatabase studentDatabase, QueryParser queryParser) {
        List<StudentRecord> filteredStudents = new ArrayList<>();

        if (queryParser.isDirectQuery()) {
            System.out.println("Using index for record retrieval."); // Printed to user so that index was used in last query
            StudentRecord studentRecord = studentDatabase.forJMBAG(queryParser.getQueriedJMBAG());
            if (studentRecord != null)
                filteredStudents.add(studentRecord);
            return filteredStudents;
        } else {
            filteredStudents.addAll(studentDatabase.filter(new QueryFilter(queryParser.getQuery())));
        }

        return filteredStudents;
    }

}
