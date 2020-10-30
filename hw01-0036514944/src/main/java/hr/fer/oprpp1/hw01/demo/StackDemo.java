package hr.fer.oprpp1.hw01.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {

    public static void main(String[] args) {

        if (args.length != 1)
            throw new IllegalArgumentException("Number of arguments must be 1.");

        String expression = args[0];
        String[] expressionElements = expression.split(" ");

        ObjectStack stack = new ObjectStack();
        for (String element : expressionElements) {
            int temp;
            try {
                temp = Integer.parseInt(element);
                stack.push(temp);
            } catch (NumberFormatException e) {
                int second = (int) stack.pop();
                int first = (int) stack.pop();

                switch (element) {
                    case "+" -> stack.push(first + second);
                    case "-" -> stack.push(first - second);
                    case "*" -> stack.push(first * second);
                    case "/" -> {
                        if (second == 0)
                            throw new IllegalArgumentException("You are trying to divide with 0.");
                        stack.push(first / second);
                    }
                    case "%" -> stack.push(first % second);
                }
            }
        }

        if (stack.size() != 1)
            throw new RuntimeException("Stack size should be 1. Yours postfix expression was invalid.");

        System.out.println(stack.pop());
    }

}
