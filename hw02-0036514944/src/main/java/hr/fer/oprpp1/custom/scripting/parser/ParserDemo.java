package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;

import java.sql.SQLOutput;
import java.text.ParseException;

public class ParserDemo {
    public static void main(String[] args) {
        /* String docBody = """
                This is sample text.
                {$ FOR i 1 10 1 $}
                 This is {$= i $}-th time this message is generated.
                {$END$}
                {$FOR i 0 10 2 $}
                 sin({$=i$}^2) = {$= i i * @sin "0.000" @decfmt $}
                {$END$}
                """;
         */
        String docBody = """
                {$ FOR i 0 10 $}{$ FOR j 0 10 $}TEXT IN NESTED FOR LOOP{$END$}{$END$}""";

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();
        System.out.println(originalDocumentBody); // Should write something like original content of docBody

    }
}
