package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.buttons.*;
import hr.fer.oprpp1.gui.calc.model.CalcModel;
import hr.fer.oprpp1.gui.calc.model.CalcModelImpl;
import hr.fer.oprpp1.gui.layouts.CalcLayout;
import hr.fer.oprpp1.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Class for Calculator GUI.
 */
public class Calculator extends JFrame {

    private static final long serialVersionUIT = 1L;

    private CalcModel calculator = new CalcModelImpl();
    private Stack<Double> stack = new Stack<>();


    public Calculator() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(5));
        cp.setBackground(Color.decode("#f2f2f2"));

        /* Adding display label */
        cp.add(new DisplayLabel("0", calculator), new RCPosition(1, 1));

        /* Adding DigitButtons */
        addDigitButtons(cp);

        /* Adding UnaryOperationButtons */
        addInvertibleUnaryOperationButtons(cp);

        /* Adding BinaryOperationButtons */
        addBinaryOperationButtons(cp);

        /* Adding OperationButtons */
        addOperationButtons(cp);

        /* Adding SpecialButtons */
        addSpecialButtons(cp);

        addCheckBox(cp);

    }

    /* This checkBox is placed here so all methods from this class have access to it, and register to this checkBox */
    private JCheckBox inverseCheckBox = new JCheckBox("Inv");

    private void addCheckBox(Container cp) {
        cp.add(inverseCheckBox, new RCPosition(5, 7));
    }

    /**
     * Two special buttons for stack implementation are added. These two buttons that will represent methods push and
     * pop will use Stack that is stored in this Class (Calculator).
     *
     * @param cp
     */
    private void addSpecialButtons(Container cp) {
        cp.add(new SpecialButton("push", e -> stack.push(calculator.getValue())), new RCPosition(3, 7));
        cp.add(new SpecialButton("pop", e -> {
            if (!stack.isEmpty())
                calculator.setValue(stack.pop());
            else
                throw new EmptyStackException();
        }), new RCPosition(4, 7));
    }

    /**
     * This method will add all OperationButton. Every button will have its text and Operator that is stored in Operator
     * class as a static final IOperator and will be applied when that button is clicked.
     *
     * @param cp
     */
    private void addOperationButtons(Container cp) {
        cp.add(new OperationButton("+/-", calculator, Operator.SWAP_SIGN), new RCPosition(5, 4));
        cp.add(new OperationButton(".", calculator, CalcModel::insertDecimalPoint), new RCPosition(5, 5));
        cp.add(new OperationButton("=", calculator, Operator.EQUALS), new RCPosition(1, 6));
        cp.add(new OperationButton("clr", calculator, Operator.CLEAR), new RCPosition(1, 7));
        cp.add(new OperationButton("reset", calculator, Operator.RESET), new RCPosition(2, 7));
    }

    /**
     * This method will create new BinaryOperationButton and add it to appropriate position in the layout.
     *
     * @param cp
     */
    private void addBinaryOperationButtons(Container cp) {
        /*
            Each BinaryOperationButton will have its own text and lambda that will be applied depending on what that
            button represents.
         */
        cp.add(new BinaryOperationButton("+", calculator, (left, right) -> left + right), new RCPosition(5, 6));
        cp.add(new BinaryOperationButton("-", calculator, (left, right) -> left - right), new RCPosition(4, 6));
        cp.add(new BinaryOperationButton("*", calculator, (left, right) -> left * right), new RCPosition(3, 6));
        cp.add(new BinaryOperationButton("/", calculator, (left, right) -> left / right), new RCPosition(2, 6));
        cp.add(registerBinaryButton(new InvertibleBinaryOperationButton("x^n", "x^(1/n)", calculator, (left, right) -> Math.pow(left, right), (left, right) -> Math.pow(left, 1 / right))), new RCPosition(5, 1));
    }

    /**
     * Registers given Invertible type of BinaryOperationButton to inverseCheckBox. It does that by adding
     * ActionListener to inverseCheckBox.
     *
     * @param button
     * @return button passed through parameter
     */
    private InvertibleBinaryOperationButton registerBinaryButton(InvertibleBinaryOperationButton button) {
        inverseCheckBox.addActionListener(e -> {
            /*
            InvertibleBinaryOperationButton has boolean flag if inverseCheckBox is selected, and it will change its
            text and method to apply accordingly. InvertibleBinaryOperationButton stores both texts for button and
            both lambdas to apply, and those will be selected depending on the state of inverseCheckBox.
             */
            boolean isSelected = inverseCheckBox.isSelected();
            button.invert(isSelected);
        });
        return button;
    }

    /**
     * This method will create new InvertibleUnaryOperationButton, register it to inverseCheckBox and add it to
     * appropriate position in the layout.
     *
     * @param cp
     */
    private void addInvertibleUnaryOperationButtons(Container cp) {
        /*
        Each InvertibleUnaryOperationButton has two texts for button (basic and inverted), and also two lambdas (basic and inverted).
        It will be registered to inverseCheckBox so text and lambda will change accordingly.
         */
        cp.add(registerUnaryButton(new InvertibleUnaryOperationButton("sin", "arcsin", calculator, Math::sin, Math::asin)), new RCPosition(2, 2));
        cp.add(registerUnaryButton(new InvertibleUnaryOperationButton("cos", "arccos", calculator, Math::cos, Math::acos)), new RCPosition(3, 2));
        cp.add(registerUnaryButton(new InvertibleUnaryOperationButton("tan", "arctan", calculator, Math::tan, Math::atan)), new RCPosition(4, 2));
        cp.add(registerUnaryButton(new InvertibleUnaryOperationButton("ctg", "arcctg", calculator, operand -> 1.0 / Math.tan(operand), Math::tan)), new RCPosition(5, 2));
        cp.add(registerUnaryButton(new InvertibleUnaryOperationButton("1/x", "1/x", calculator, operand -> Math.pow(operand, -1), operand -> Math.pow(operand, -1))), new RCPosition(2, 1));
        cp.add(registerUnaryButton(new InvertibleUnaryOperationButton("ln", "e^x", calculator, Math::log, Math::exp)), new RCPosition(4, 1));
        cp.add(registerUnaryButton(new InvertibleUnaryOperationButton("log", "10^x", calculator, Math::log10, operand -> Math.pow(10, operand))), new RCPosition(3, 1));
    }

    /**
     * Registers given Invertible type of UnaryOperationButton to inverseCheckBox. It does that by adding ActionListener
     * to inverseCheckBox.
     *
     * @param button
     * @return button passed through parameter
     */
    private InvertibleUnaryOperationButton registerUnaryButton(InvertibleUnaryOperationButton button) {
        inverseCheckBox.addActionListener(e -> {
            /*
            InvertibleUnaryOperationButton has boolean flag if inverseCheckBox is selected, and it will change its
            text and method to apply accordingly. InvertibleUnaryOperationButton stores both texts for button and
            both lambdas to apply, and those will be selected depending on the state of inverseCheckBox.
             */
            boolean isSelected = inverseCheckBox.isSelected();
            button.invert(isSelected);
        });
        return button;
    }

    /**
     * Adds all 10 digits to GUI. It also adds lambdas that will insert digits to calculator model depending on what
     * number they represent.
     *
     * @param cp
     */
    private void addDigitButtons(Container cp) {
        cp.add(new DigitButton("0", calculator, model -> model.insertDigit(0)), new RCPosition(5, 3));
        cp.add(new DigitButton("1", calculator, model -> model.insertDigit(1)), new RCPosition(4, 3));
        cp.add(new DigitButton("2", calculator, model -> model.insertDigit(2)), new RCPosition(4, 4));
        cp.add(new DigitButton("3", calculator, model -> model.insertDigit(3)), new RCPosition(4, 5));
        cp.add(new DigitButton("4", calculator, model -> model.insertDigit(4)), new RCPosition(3, 3));
        cp.add(new DigitButton("5", calculator, model -> model.insertDigit(5)), new RCPosition(3, 4));
        cp.add(new DigitButton("6", calculator, model -> model.insertDigit(6)), new RCPosition(3, 5));
        cp.add(new DigitButton("7", calculator, model -> model.insertDigit(7)), new RCPosition(2, 3));
        cp.add(new DigitButton("8", calculator, model -> model.insertDigit(8)), new RCPosition(2, 4));
        cp.add(new DigitButton("9", calculator, model -> model.insertDigit(9)), new RCPosition(2, 5));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator().setVisible(true);
        });
    }

}
