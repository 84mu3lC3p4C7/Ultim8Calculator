package com.example.ultim8calculator;


import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import algorithm.ReversePolishNotation;

public class Calculator {

    Activity activity;
    TextView inputOutputLine, calculationLine, brackets1, brackets2;
    Button clearAllButton, commaButton, closingBracketButton, squareRootButton;
    ReversePolishNotation rpn = new ReversePolishNotation();
    NumberFormat decimalFormat = DecimalFormat.getInstance();
    String text; // just to get rid of warnings "Do not concatenate text displayed with setText. Use resource string with placeholders."
    double result;
    int openingBracketsCount = 0, closingBracketsCount = 0;
    boolean operationBefore = false, newCalculation = true, operationDeleted = false;
    public boolean isOutputNumber = true;

    public Calculator(Activity activity, String[] loadedData) {
        this.activity = activity;
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setMaximumFractionDigits(8);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        setupCalculator(loadedData);
    }
    private void setupCalculator(String[] loadedData) {
        inputOutputLine = activity.findViewById(R.id.txt_inputOutputLine);
        calculationLine = activity.findViewById(R.id.txt_calculationLine);
        brackets1 = activity.findViewById(R.id.num_brackets1);
        brackets2 = activity.findViewById(R.id.num_brackets2);
        clearAllButton = activity.findViewById(R.id.btn_delete1);
        commaButton = activity.findViewById(R.id.btn_comma);
        squareRootButton = activity.findViewById(R.id.btn_operation6);
        closingBracketButton = activity.findViewById(R.id.btn_bracket2);

        if (loadedData != null && loadedData.length > 0) {
            try {
                inputOutputLine.setText(loadedData[0]);
                calculationLine.setText(loadedData[1]);
                result = Double.parseDouble(loadedData[2]);
                openingBracketsCount = Integer.parseInt(loadedData[3]);
                brackets1.setText(String.valueOf(openingBracketsCount));
                closingBracketsCount = Integer.parseInt(loadedData[4]);
                brackets2.setText(String.valueOf(closingBracketsCount));
                if (openingBracketsCount == closingBracketsCount) {
                    closingBracketButton.setEnabled(false);
                }
                operationBefore = Boolean.parseBoolean(loadedData[5]);
                newCalculation = Boolean.parseBoolean(loadedData[6]);
                operationDeleted = Boolean.parseBoolean(loadedData[7]);
                if (!Boolean.parseBoolean(loadedData[8])) {
                    commaButton.setEnabled(false);
                }
                if (!Boolean.parseBoolean(loadedData[9])) {
                    squareRootButton.setEnabled(false);
                }
                clearAllButton.setText(loadedData[10]);
            } catch (ArrayIndexOutOfBoundsException e) {
                inputOutputLine.setText("0");
                closingBracketButton.setEnabled(false);
                commaButton.setEnabled(true);
                squareRootButton.setEnabled(true);
                System.err.println("Failed to load calculator data: " + e.getLocalizedMessage());
            }
        }
        else {
            inputOutputLine.setText("0");
            closingBracketButton.setEnabled(false);
            commaButton.setEnabled(true);
        }
    }

    private String getInputOutputLineText() {
        return inputOutputLine.getText().toString();
    }

    private String getCalculationLineText() {
        return calculationLine.getText().toString();
    }

    private void afterNewCalculation() {
        newCalculation = false;
        openingBracketsCount = 0;
        brackets1.setText(String.valueOf(openingBracketsCount));
        closingBracketsCount = 0;
        brackets2.setText(String.valueOf(closingBracketsCount));
        closingBracketButton.setEnabled(false);
        commaButton.setEnabled(true);
        squareRootButton.setEnabled(true);
        calculationLine.setText("");
    }

    public void fixNonNumericOutput() {
        isOutputNumber = true;
        afterNewCalculation();
        inputOutputLine.setText("0");
    }

    public void addNumberToInputOutputLine(View view) {
        Button btn = (Button) view;
        if (newCalculation) {
            afterNewCalculation();
        }
        operationBefore = false;
        text = "C";
        clearAllButton.setText(text);
        text = getInputOutputLineText();
        if (text.equals("0")) {
            text = btn.getText().toString();
        }
        else if (text.length() > 1 && text.equals("-0")) {
            text = text.substring(0, text.length() - 1) + btn.getText();
        }
        else {
            text = text + btn.getText().toString();
        }
        inputOutputLine.setText(text);
    }

    public void addOperationToCalculationLine(View view) {
        if (!getInputOutputLineText().isEmpty()) {
            if (newCalculation) {
                afterNewCalculation();
            }
            Button btn = (Button) view;
            if (operationBefore) {
                text = getCalculationLineText().substring(0, getCalculationLineText().length() - 2) + btn.getText().toString() + " ";
                calculationLine.setText(text);
            }
            else {
                operationBefore = true;
                commaButton.setEnabled(true);
                squareRootButton.setEnabled(true);
                if (!getCalculationLineText().isEmpty() && rpn.isNumber(getCalculationLineText().substring(getCalculationLineText().length() - 1)) || !getCalculationLineText().isEmpty() && getCalculationLineText().substring(getCalculationLineText().length() - 1).equals(")")) {
                    operationDeleted = false;
                    text = getCalculationLineText() + " " + btn.getText().toString() + " ";
                }
                else {
                    text = getCalculationLineText() + getInputOutputLineText() + " " + btn.getText().toString() + " ";
                }
                calculationLine.setText(text);
                inputOutputLine.setText("0");
                text = "AC";
                clearAllButton.setText(text);
            }
        }
    }

    public void negation() {
        text = getInputOutputLineText();
        if (newCalculation) {
            afterNewCalculation();
            if (text.equals("0")) {
                inputOutputLine.setText("-0");
            }
            else {
                text = "-" + text;
                calculationLine.setText(text);
                inputOutputLine.setText("0");
            }
        }
        else {
            if (!text.startsWith("-")) {
                text = "-" + text;
                inputOutputLine.setText(text);
            }
            else {
                text = text.substring(1);
                inputOutputLine.setText(text);
            }
        }
    }

    public void addSquareRoot() {
        if (newCalculation) {
            afterNewCalculation();
            if (!getInputOutputLineText().equals("0")) {
                text = "√" + getInputOutputLineText();
                calculationLine.setText(text);
                inputOutputLine.setText("0");
            }
            else {
                calculationLine.setText("√");
            }
            squareRootButton.setEnabled(false);
        }
        else {
            text = getCalculationLineText();
            squareRootButton.setEnabled(false);
            operationBefore = false;
            text += "√";
            calculationLine.setText(text);
        }
    }

    public void addBracketToCalculationLine(View view) {
        Button btn = (Button) view;
        if (newCalculation) {
            afterNewCalculation();
        }
        if (btn.getText().toString().equals("(")) {
            openingBracketsCount++;
            brackets1.setText(String.valueOf(openingBracketsCount));
            closingBracketButton.setEnabled(true);
            text = getInputOutputLineText();
            if (!text.equals("0")) {
                text = getCalculationLineText() + text + " × " + btn.getText() + " ";
                inputOutputLine.setText("0");
            }
            else if (!getCalculationLineText().isEmpty() && rpn.isNumber(getCalculationLineText().substring(getCalculationLineText().length() - 1)) || !getCalculationLineText().isEmpty() && getCalculationLineText().substring(getCalculationLineText().length() - 1).equals(")")) {
                text = getCalculationLineText() + " × " + btn.getText() + " ";
            }
            else {
                text = getCalculationLineText() + btn.getText() + " ";
            }
        }
        else {
            closingBracketsCount++;
            brackets2.setText(String.valueOf(closingBracketsCount));
            if (openingBracketsCount == closingBracketsCount) {
                closingBracketButton.setEnabled(false);
            }
            operationDeleted = true;
            if (getCalculationLineText().substring(getCalculationLineText().length() - 2, getCalculationLineText().length() - 1).matches("[+\\-×÷^(]")) {
                text = getCalculationLineText() + getInputOutputLineText() + " " + btn.getText();
            }
            else {
                text = getCalculationLineText() + " " + btn.getText();
            }
            inputOutputLine.setText("0");
        }
        operationBefore = false;
        calculationLine.setText(text);
    }

    public void finishCalculation() {
        if (!newCalculation) {
            newCalculation = true;
            if (getCalculationLineText().isEmpty()) {
                calculationLine.setText(getInputOutputLineText());
            }
            else {
                text = getCalculationLineText();
                if (text.length() > 1 && text.substring(text.length() - 2, text.length() - 1).matches("[+\\-×÷^(]") || text.endsWith("√")) {
                    text += getInputOutputLineText();
                }
                for (int i = openingBracketsCount; i > closingBracketsCount; i--) {
                    text += " )";
                }
                closingBracketsCount = openingBracketsCount;
                brackets2.setText(String.valueOf(closingBracketsCount));
                calculationLine.setText(text);
            }
            result = rpn.solveRPN(rpn.convertToRPN(getCalculationLineText().replace(",", ".")));
            if (!Double.isFinite(result)) {
                isOutputNumber = false;
            }
            if (isCommaNecessary(result)) {
                commaButton.setEnabled(false);
                text = decimalFormat.format(result); //used to be "text = decimalFormat.format(result).replace(",", "").replace('.', ',');", but physical devices (at least mine) automatically replaces dot with comma
                inputOutputLine.setText(text);
                text = getCalculationLineText() + " =";
                calculationLine.setText(text);
            }
            else {
                commaButton.setEnabled(true);
                text = getCalculationLineText() + " =";
                calculationLine.setText(text);
                inputOutputLine.setText(String.valueOf((long) result));
            }
            squareRootButton.setEnabled(true);
            closingBracketButton.setEnabled(false);
            operationBefore = false;
            text = "AC";
            clearAllButton.setText(text);
        }
    }

    public void addCommaToInputOutputLine() {
        if (newCalculation) {
            afterNewCalculation();
        }
        commaButton.setEnabled(false);
        text = getInputOutputLineText() + ",";
        inputOutputLine.setText(text);
    }

    public void backspace() {
        if (newCalculation) {
            newCalculation = false;
            operationDeleted = true;
            closingBracketButton.setEnabled(false);
            commaButton.setEnabled(true);
            if (!getCalculationLineText().isEmpty()) {
                text = getCalculationLineText().substring(0, getCalculationLineText().length() - 2);
                calculationLine.setText(text);
            }
            text = "0";
            inputOutputLine.setText(text);
        }
        else {
            if (!getInputOutputLineText().equals("0")) {
                text = getInputOutputLineText();
                if (text.substring(text.length() - 1).equals(",")) {
                    commaButton.setEnabled(true);
                }
                text = text.substring(0, text.length() - 1);
                if (text.isEmpty()) {
                    text = "0";
                }
                inputOutputLine.setText(text);
            }
            else if (!getCalculationLineText().isEmpty()) {
                text = getCalculationLineText();
                if (!rpn.isNumber(text.substring(text.length() - 1))) {
                    if (text.endsWith("√")) {
                        squareRootButton.setEnabled(true);
                        text = text.substring(0, text.length() - 1);
                        if (!text.isEmpty()) {
                            operationBefore = true;
                        }
                    }
                    else if (text.substring(text.length() - 2, text.length() - 1).equals("(") || text.substring(text.length() - 1).equals(")")) {
                        if (text.substring(text.length() - 2, text.length() - 1).equals("(")) {
                            openingBracketsCount--;
                            brackets1.setText(String.valueOf(openingBracketsCount));
                            if (openingBracketsCount == closingBracketsCount) {
                                closingBracketButton.setEnabled(false);
                            }
                            operationBefore = true;
                        }
                        else {
                            closingBracketsCount--;
                            brackets2.setText(String.valueOf(closingBracketsCount));
                            closingBracketButton.setEnabled(true);
                        }
                        text = text.substring(0, text.length() - 2);
                    }
                    else {
                        text = text.substring(0, text.length() - 3);
                        operationBefore = false;
                        operationDeleted = true;
                    }
                }
                else {
                    while (!text.isEmpty() && !text.substring(text.length() - 1).equals(" ")) {
                        text = text.substring(0, text.length() - 1);
                    }
                    operationBefore = true;
                }
                calculationLine.setText(text);
            }
        }
    }

    public void clearEverything() {
        if (getInputOutputLineText().equals("0") || newCalculation) {
            openingBracketsCount = 0;
            brackets1.setText(String.valueOf(openingBracketsCount));
            closingBracketsCount = 0;
            brackets2.setText(String.valueOf(closingBracketsCount));
            closingBracketButton.setEnabled(false);
            squareRootButton.setEnabled(true);
            commaButton.setEnabled(true);
            operationBefore = false;
            operationDeleted = false;
            calculationLine.setText("");
            inputOutputLine.setText("0");
        }
        else {
            commaButton.setEnabled(true);
            squareRootButton.setEnabled(true);
            operationBefore = false;
            text = "AC";
            clearAllButton.setText(text);
            inputOutputLine.setText("0");
        }
    }

    private boolean isCommaNecessary(double number) {
        long comparedInt = (long) number;
        return (double) comparedInt != number;
    }

    public String getCalculatorData() {
        return getInputOutputLineText() + ";" + getCalculationLineText() + ";" + result + ";" + openingBracketsCount+ ";" + closingBracketsCount + ";" + operationBefore + ";" + newCalculation + ";" + operationDeleted + ";" + commaButton.isEnabled() + ";" + squareRootButton.isEnabled() + ";" + clearAllButton.getText();
    }
}