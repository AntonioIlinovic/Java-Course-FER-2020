package hr.fer.oprpp1.custom.scripting;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.nodes.*;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptParserTest {

    private String readExample(int n) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt")) {
            if (is == null)
                throw new RuntimeException("Datoteka extra/primjer" + n + ".txt je nedostupna.");

            byte[] data = is.readAllBytes();
            String text = new String(data, StandardCharsets.UTF_8);
            return text;
        } catch (IOException ex) {
            throw new RuntimeException("Greška pir čitanju datoteke.", ex);
        }
    }

    @Test
    public void testExample_1() {
        String text = readExample(1);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode node = parser.getDocumentNode();
        assertEquals(1, node.numberOfChildren());
        Node child_1 = node.getChild(0);
        assertTrue(child_1 instanceof TextNode);
    }

    @Test
    public void testExample_2() {
        String text = readExample(2);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode node = parser.getDocumentNode();
        assertEquals(1, node.numberOfChildren());
        Node child_1 = node.getChild(0);
        assertTrue(child_1 instanceof TextNode);
    }

    @Test
    public void testExample_3() {
        String text = readExample(3);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode node = parser.getDocumentNode();
        assertEquals(1, node.numberOfChildren());
        Node child_1 = node.getChild(0);
        assertTrue(child_1 instanceof TextNode);
    }

    @Test
    public void testExample_4() {
        String text = readExample(4);
        assertThrows(SmartScriptParserException.class, () -> {
            SmartScriptParser parser = new SmartScriptParser(text);
        });
    }

    @Test
    public void testExample_5() {
        String text = readExample(5);
        assertThrows(SmartScriptParserException.class, () -> {
            SmartScriptParser parser = new SmartScriptParser(text);
        });
    }

    @Test
    public void testExample_6() {
        String text = readExample(6);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode node = parser.getDocumentNode();
        assertEquals(3, node.numberOfChildren());
        Node child_1 = node.getChild(0);
        Node child_2 = node.getChild(1);
        Node child_3 = node.getChild(2);
        assertTrue(child_1 instanceof TextNode);
        assertTrue(child_2 instanceof EchoNode);
        assertTrue(child_3 instanceof TextNode);

        assertEquals(1, ((EchoNode) child_2).getElements().length);
        Element child_2__element_1 = ((EchoNode) child_2).getElements()[0];
        assertTrue(child_2__element_1 instanceof ElementString);
    }

    @Test
    public void testExample_7() {
        String text = readExample(7);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode node = parser.getDocumentNode();
        assertEquals(3, node.numberOfChildren());
        Node child_1 = node.getChild(0);
        Node child_2 = node.getChild(1);
        Node child_3 = node.getChild(2);
        assertTrue(child_1 instanceof TextNode);
        assertTrue(child_2 instanceof EchoNode);
        assertTrue(child_3 instanceof TextNode);

        assertEquals(1, ((EchoNode) child_2).getElements().length);
        Element child_2__element_1 = ((EchoNode) child_2).getElements()[0];
        assertTrue(child_2__element_1 instanceof ElementString);
    }

    @Test
    public void testExample_8() {
        String text = readExample(8);
        assertThrows(SmartScriptParserException.class, () -> {
            SmartScriptParser parser = new SmartScriptParser(text);
        });
    }

    @Test
    public void testExample_9() {
        String text = readExample(9);
        assertThrows(SmartScriptParserException.class, () -> {
            SmartScriptParser parser = new SmartScriptParser(text);
        });
    }

    @Test
    public void testExample_10() {
        String text = readExample(10);

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode node = parser.getDocumentNode();
        assertEquals(7, node.numberOfChildren());

        ForLoopNode outerForLoop = (ForLoopNode) node.getChild(5);
        assertEquals(3, outerForLoop.numberOfChildren());
    }

    @Test
    public void testParserToStringWillBeParsedAgainEqually() {
        String text = readExample(10);

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();

        SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
        DocumentNode document2 = parser2.getDocumentNode();
        // Now document and document2 should be structurally identical trees
        boolean same = document.equals(document2);
        assertTrue(same);
    }
}
