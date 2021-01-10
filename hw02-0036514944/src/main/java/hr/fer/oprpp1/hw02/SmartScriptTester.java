package hr.fer.oprpp1.hw02;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class SmartScriptTester {
    public static void main(String[] args) throws IOException {

        String filepath = "src/test/resources/extra/primjer10.txt";

        String docBody = Files.readString(Paths.get(filepath));
        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();
        System.out.println(originalDocumentBody); // should write something like original
        // content of docBody

    }
}
