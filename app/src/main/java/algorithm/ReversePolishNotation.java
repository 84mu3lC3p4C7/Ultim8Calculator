package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class ReversePolishNotation {

    // Making this was pain

    public String convertToRPN(String calculation) {
        if (!calculation.contains(" ") && !calculation.contains("√")) {
            return calculation;
        }
        else {
            String[] splitCalculation = calculation.split("\\s+");
            StringBuilder rpn = new StringBuilder();
            Stack<String> stack = new Stack<>();
            for (String s : splitCalculation) {
                if (s.charAt(0) == '√') {
                    s = solveSquareRoot(s);
                }
                if (isNumber(s)) {
                    rpn.append(s).append(" ");
                }
                else {
                    if (stack.isEmpty()) {
                        stack.add(s);
                    }
                    else {
                        if (s.equals(")")) {
                            while (!stack.isEmpty() && !stack.lastElement().equals("(")) {
                                rpn.append(stack.pop()).append(" ");
                            }
                            if (!stack.isEmpty()) {
                                stack.pop();
                            }
                        }
                        else if (s.equals("(") || getPrecedence(stack.lastElement()) == 0 || getPrecedence(stack.lastElement()) < getPrecedence(s)) {
                            stack.push(s);
                        }
                        else {
                            rpn.append(stack.pop()).append(" ");
                            stack.push(s);
                        }
                    }
                }
            }
            while (!stack.isEmpty()) {
                rpn.append(stack.pop()).append(" ");
            }
            return rpn.toString();
        }
    }

    public Double solveRPN(String rpn) {
        if (!rpn.contains(" ")) {
            return Double.parseDouble(rpn);
        }
        else {
            ArrayList<String> items = new ArrayList<>(Arrays.asList(rpn.split(" ")));
            ArrayList<Double> numbers = new ArrayList<>();
            for (String item : items) {
                if (isNumber(item)) {
                    numbers.add(Double.parseDouble(item));
                }
                else {
                    switch (item) {
                        case("+"):
                            numbers.set(numbers.size() - 2, numbers.get(numbers.size() - 2) + numbers.get(numbers.size() - 1));
                            break;
                        case("-"):
                            numbers.set(numbers.size() - 2, numbers.get(numbers.size() - 2) - numbers.get(numbers.size() - 1));
                            break;
                        case("×"):
                            numbers.set(numbers.size() - 2, numbers.get(numbers.size() - 2) * numbers.get(numbers.size() - 1));
                            break;
                        case("÷"):
                            numbers.set(numbers.size() - 2, numbers.get(numbers.size() - 2) / numbers.get(numbers.size() - 1));
                            break;
                        case("^"):
                            numbers.set(numbers.size() - 2, Math.pow(numbers.get(numbers.size() - 2), numbers.get(numbers.size() - 1)));
                            break;
                    }
                    numbers.remove(numbers.size() - 1);
                }
            }
            return numbers.get(0);
        }
    }

    private int getPrecedence(String s) {
        switch (s) {
            case ("+"):
            case ("-"):
                return 1;
            case ("×"):
            case ("÷"):
                return 2;
            case ("^"):
                return 3;
            default:
                return 0;
        }
    }

    private String solveSquareRoot(String s) {
        return String.valueOf(Math.sqrt(Double.parseDouble(s.substring(1))));
    }

    public boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        if (string.equals("∞")) {
            return true;
        }
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
