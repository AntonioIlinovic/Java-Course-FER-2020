package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException;

import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;


/**
 * Parser implementation for SmartScript language by recursive descent.
 */
public class SmartScriptParser {

    private final SmartScriptLexer tokenizer;       // Lexer (tokenizer) of source code.
    private DocumentNode documentNode;              // Tree which represents parsed program.
    /* ObjectStack which will help for tree construction. At the start of parsing, DocumentNode is pushed to it. Then,
       for each empty tag or text node we create that tag/node and add it as a child of Node that was last pushed on the
       stack. */
    private ObjectStack treeStack;

    /**
     * Constructor. Parser has a single constructor which accepts a string that contains document body. In this
     * constructor, parser creates an instance of lexer and initializes it with obtained text. The parser uses lexer for
     * production of tokens. The constructor delegates actual parsing to separate method (in the same class). This
     * allows us to later add different constructors that will retrieve documents by various means and delegate the
     * parsing to the same method.
     *
     * @param text program text which will be tokenized and parsed
     * @throws SmartScriptLexerException  in case of error in tokenizing
     * @throws SmartScriptParserException in case of error in parsing
     */
    public SmartScriptParser(String text) {
        if (text == null)
            throw new SmartScriptLexerException("Text of program can't be empty");

        this.tokenizer = new SmartScriptLexer(text);
        try {
            documentNode = parse();
        } catch (Exception e) {
            throw new SmartScriptParserException("Error occurred in parse method: " + e.getMessage());
        }
    }

    /**
     * Getter for tree made by parsing source code.
     *
     * @return program tree
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }

    /**
     * Helper method which returns TokenType of current Token.
     *
     * @return TokenType of current Token
     */
    private SmartScriptTokenType getCurrentTokenType() {
        return tokenizer.getToken().getTokenType();
    }

    /**
     * Helper method which is an parser implementation by recursive descent.
     *
     * @return program tree
     */
    private DocumentNode parse() {
        documentNode = new DocumentNode();
        treeStack = new ObjectStack();
        /* At the start documentNode is pushed to the stack. Then, for each empty tag ('=' tag is an empty tag)
           or text node create that tag/node and add it as a child of Node that was last pushed on the stack.
           If you encounter non-empty tag (i.e. FOR tag), create it, add it as a child of Node that was last
           pushed on the stack and then push this FOR-node to the stack. Now all nodes following will be added
           as children of this FOR-node. The exception is {$END$}. When you encounter it, simple pop one entry
           from the stack. If stack remains empty, there is error in document - it contains more {$END$}-s
           than opened non-empty tags, so throw an exception. */
        treeStack.push(documentNode);
        // Generate first token at the start of the parsing
        tokenizer.nextToken();

        // Add all nodes in program (until you get token of type EOF) to DocumentNode and return it
        while (getCurrentTokenType() != SmartScriptTokenType.EOF) {

            if (getCurrentTokenType() == SmartScriptTokenType.TAGSTART) {
                // TAGSTART: if at the start of a tag parse it as a tag
                tokenizer.setState(SmartScriptLexerState.TAG);
                tokenizer.nextToken();
                parseTag();
            } else if (getCurrentTokenType() == SmartScriptTokenType.TAGEND) {
                // TAGEND: if at the end of a tag, just generate next token
                tokenizer.setState(SmartScriptLexerState.TEXT);
                tokenizer.nextToken();
            } else if (getCurrentTokenType() == SmartScriptTokenType.TEXT) {
                /* TEXT: add it to last object on the stack as a child. */
                String text = tokenizer.getToken().getValue().toString();
                TextNode textNode = new TextNode(text);

                Node lastNodeOnStack = (Node) treeStack.peek();
                lastNodeOnStack.addChildNode(textNode);
                tokenizer.nextToken();
            }
        }

        // There must be exactly one element on stack at the end of parsing (DocumentNode)
        if (treeStack.size() != 1)
            throw new SmartScriptParserException("Your document must have exactly one closing tag for each non-empty tag");
        return documentNode;
    }

    /**
     * Helper method which parses incoming tokens in tag mode.
     */
    private void parseTag() {
        /* We check which type of tag it is, and depending on that call its method. */
        if (getCurrentTokenType() == SmartScriptTokenType.TAGNAME) {
            String tagname = tokenizer.getToken().getValue().toString();

            if (tagname.equals("=")) {
                tokenizer.nextToken();
                parseEmptyTag();
            }
            if (tagname.equalsIgnoreCase("FOR")) {
                tokenizer.nextToken();
                parseForLoop();
            }
            if (tagname.equalsIgnoreCase("END")) {
                /* If TAGNAME is 'END' just pop one Node from stack. */
                tokenizer.nextToken();
                treeStack.pop();
                if (treeStack.isEmpty())
                    throw new SmartScriptParserException("There is more closing tags ('END') than opening tags");
            }
        } else {
            /* After TAGSTART there must be TAGNAME tag */
            throw new SmartScriptParserException("After opening tag you must have a name for that tag (i.e. 'FOR', 'END', '='");
        }
    }

    /**
     * Add all elements to one EchoNode until you get to TAGEND. When you get to the end of the tag, put that EchoNode
     * with all that previous tokens as a child to the last object on stack.
     */
    private void parseEmptyTag() {
        /* This array will store all elements that are to be stored in EchoNode. Just before we put that
        EchoNode to the last object on stack as a child, convert it to Elements[]. */
        ArrayIndexedCollection elements = new ArrayIndexedCollection();

        /* Get tokens until you get to the end of the tag. */
        while (getCurrentTokenType() != SmartScriptTokenType.TAGEND) {

            if (getCurrentTokenType() == SmartScriptTokenType.VARIABLE) {
                /* VARIABLE: make new ElementVariable, and add it to elements. */
                String variableName = tokenizer.getToken().getValue().toString();
                ElementVariable elementVariable = new ElementVariable(variableName);
                elements.add(elementVariable);
            }
            if (getCurrentTokenType() == SmartScriptTokenType.OPERATOR) {
                /* OPERATOR: make new ElementOperator, and add it to elements. */
                String operatorName = tokenizer.getToken().getValue().toString();
                ElementOperator elementOperator = new ElementOperator(operatorName);
                elements.add(elementOperator);
            }
            if (getCurrentTokenType() == SmartScriptTokenType.FUNCTION) {
                /* FUNCTION: make new ElementFunction, and add it to elements. */
                String functionName = tokenizer.getToken().getValue().toString();
                ElementFunction elementFunction = new ElementFunction(functionName);
                elements.add(elementFunction);
            }
            if (getCurrentTokenType() == SmartScriptTokenType.STRING) {
                /* STRING: make new ElementString, and add it to elements. */
                String string = tokenizer.getToken().getValue().toString();
                ElementString elementString = new ElementString(string);
                elements.add(elementString);
            }
            if (getCurrentTokenType() == SmartScriptTokenType.INTEGER) {
                /* INTEGER: make new ElementInteger, and add it to elements. */
                int anInt = Integer.parseInt(tokenizer.getToken().getValue().toString());
                ElementConstantInteger elementConstantInteger = new ElementConstantInteger(anInt);
                elements.add(elementConstantInteger);
            }
            if (getCurrentTokenType() == SmartScriptTokenType.DOUBLE) {
                /* DOUBLE: make new ElementDouble, and add it to elements. */
                double aDouble = Double.parseDouble(tokenizer.getToken().getValue().toString());
                ElementConstantDouble elementConstantDouble = new ElementConstantDouble(aDouble);
                elements.add(elementConstantDouble);
            }

            tokenizer.nextToken();
        }

        // Copy from ArrayIndexedCollection to Element[]
        Element[] echoNodeElements = new Element[elements.size()];
        for (int index = 0; index < elements.size(); index++)
            echoNodeElements[index] = (Element) elements.get(index);

        EchoNode echoNode = new EchoNode(echoNodeElements);

        Node lastNodeOnStack = (Node) treeStack.peek();
        lastNodeOnStack.addChildNode(echoNode);
    }

    /**
     * Add 3 or 4 elements to one ForLoopNode. Then add that ForLoopNode to the last object on stack. Lastly push this
     * ForLoopNode on stack.
     */
    private void parseForLoop() {
        /* First Element of for loop must be of type ElementVariable. */
        if (getCurrentTokenType() != SmartScriptTokenType.VARIABLE)
            throw new SmartScriptParserException("First Element of for loop must be of type ElementVariable");
        String variableName = tokenizer.getToken().getValue().toString();
        ElementVariable first = new ElementVariable(variableName);
        tokenizer.nextToken();

        Element second = createNewElementOfCorrectType();
        tokenizer.nextToken();

        Element third = createNewElementOfCorrectType();
        tokenizer.nextToken();

        Element fourth = null;
        /* Check if there is 4th optional for loop argument. */
        if (getCurrentTokenType() != SmartScriptTokenType.TAGEND) {
            fourth = createNewElementOfCorrectType();
            tokenizer.nextToken();
        }

        /* For loop must have 3 or 4 arguments. So current tag must be TAGEND. */
        if (getCurrentTokenType() != SmartScriptTokenType.TAGEND)
            throw new SmartScriptParserException("For loop must accept 3 or 4 arguments");

        ForLoopNode forLoopNode = new ForLoopNode(first, second, third, fourth);
        Node lastNodeOnStack = (Node) treeStack.peek();
        lastNodeOnStack.addChildNode(forLoopNode);
        treeStack.push(forLoopNode);
    }

    /**
     * Helper method which creates Element of correct type, depending on current token type.
     *
     * @return Element of correct type.
     */
    private Element createNewElementOfCorrectType() {
        if (getCurrentTokenType() == SmartScriptTokenType.VARIABLE) {
            /* VARIABLE: make new ElementVariable. */
            String variableName = tokenizer.getToken().getValue().toString();
            return new ElementVariable(variableName);
        }
        if (getCurrentTokenType() == SmartScriptTokenType.OPERATOR) {
            /* OPERATOR: make new ElementOperator. */
            String operatorName = tokenizer.getToken().getValue().toString();
            return new ElementOperator(operatorName);
        }
        if (getCurrentTokenType() == SmartScriptTokenType.FUNCTION) {
            /* FUNCTION: make new ElementFunction. */
            String functionName = tokenizer.getToken().getValue().toString();
            return new ElementFunction(functionName);
        }
        if (getCurrentTokenType() == SmartScriptTokenType.STRING) {
            /* STRING: make new ElementString. */
            String string = tokenizer.getToken().getValue().toString();
            return new ElementString(string);
        }
        if (getCurrentTokenType() == SmartScriptTokenType.INTEGER) {
            /* INTEGER: make new ElementInteger. */
            int anInt = Integer.parseInt(tokenizer.getToken().getValue().toString());
            return new ElementConstantInteger(anInt);
        }
        if (getCurrentTokenType() == SmartScriptTokenType.DOUBLE) {
            /* DOUBLE: make new ElementDouble. */
            double aDouble = Double.parseDouble(tokenizer.getToken().getValue().toString());
            return new ElementConstantDouble(aDouble);
        }

        throw new SmartScriptParserException("Error in parsing");
    }

}
