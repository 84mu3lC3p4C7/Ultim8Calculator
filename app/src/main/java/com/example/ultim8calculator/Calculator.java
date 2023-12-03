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
    TextView inputOutputLine, calculationLine;
    Button clearAllButton, commaButton, closingBracketButton;
    ReversePolishNotation rpn = new ReversePolishNotation();
    NumberFormat decimalFormat = DecimalFormat.getInstance();
    String text; // just to get rid of warnings "Do not concatenate text displayed with setText. Use resource string with placeholders."
    double result;
    int brackets = 0;
    boolean operationBefore = false, newCalculation = true, operationDeleted = false;

    public Calculator(Activity activity) {
        this.activity = activity;
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setMaximumFractionDigits(8);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        setupCalculator();
    }
    private void setupCalculator() {
        inputOutputLine = activity.findViewById(R.id.txt_inputOutputLine);
        calculationLine = activity.findViewById(R.id.txt_calculationLine);
        clearAllButton = activity.findViewById(R.id.btn_delete1);
        commaButton = activity.findViewById(R.id.btn_comma);
        closingBracketButton = activity.findViewById(R.id.btn_bracket2);

        text = "0";
        inputOutputLine.setText(text);
        closingBracketButton.setEnabled(false);
    }

    private String getInputOutputLineText() {
        return inputOutputLine.getText().toString();
    }

    private String getCalculationLineText() {
        return calculationLine.getText().toString();
    }

    private void afterNewCalculation() {
        newCalculation = false;
        brackets = 0;
        closingBracketButton.setEnabled(false);
        commaButton.setEnabled(true);
        calculationLine.setText("");
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
        else if (text.length() > 1 && text.startsWith("√0") || text.length() > 1 && text.startsWith("-0")) {
            text = text.substring(0, text.length() - 1) + btn.getText();
            System.out.println("hahfafhaf");
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

    public void addNegationOrSquareRoot(View view) {
        if (newCalculation) {
            afterNewCalculation();
        }
        Button btn = (Button) view;
        text = getInputOutputLineText();
        if (rpn.isNumber(String.valueOf(text.charAt(0)))) {
            if (btn.getText().equals("√")) {
                text = "√" + text;
            }
            else {
                text = "-" + text;
            }
        }
        else {
            if (btn.getText().equals("√")) {
                if (text.charAt(0) == '±') {
                    text = "√" + text.substring(1);
                }
                else {
                    text = text.substring(1);
                }
            }
            else {
                if (text.charAt(0) == '√') {
                    text = "-" + text.substring(1);
                }
                else {
                    text = text.substring(1);
                }
            }
        }
        inputOutputLine.setText(text);
    }

    public void addBracketToCalculationLine(View view) {
        Button btn = (Button) view;
        if (newCalculation) {
            afterNewCalculation();
        }
        if (btn.getText().toString().equals("(")) {
            brackets++;
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
            brackets--;
            if (brackets < 1) {
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
                if (text.length() > 1 && text.substring(text.length() - 2, text.length() - 1).matches("[+\\-×÷^(]")) {
                    text += getInputOutputLineText();
                }
                for (int i = 0; i < brackets; i++) {
                    text += " )";
                }
                calculationLine.setText(text);
                System.out.println(text);
            }
            try {
                result = rpn.solveRPN(rpn.convertToRPN(getCalculationLineText().replace(",", ".")));
            }   catch (NumberFormatException e ) {
                result = 0;
                // need to handle errors here !!!
            }
            if (isCommaNecessary(result)) {
                commaButton.setEnabled(false);
                text = decimalFormat.format(result); //used to be "text = decimalFormat.format(result).replace(",", "").replace('.', ',');", but this works for physical devices just fine
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
            brackets = 0;
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
            brackets = 0;
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
                    if (text.substring(text.length() - 2, text.length() - 1).equals("(") || text.substring(text.length() - 1).equals(")")) {
                        if (text.substring(text.length() - 2, text.length() - 1).equals("(")) {
                            brackets--;
                            if (brackets < 1) {
                                closingBracketButton.setEnabled(false);
                            }
                            operationBefore = true;
                        }
                        else {
                            brackets++;
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
            brackets = 0;
            closingBracketButton.setEnabled(false);
            commaButton.setEnabled(true);
            operationBefore = false;
            operationDeleted = false;
            calculationLine.setText("");
            inputOutputLine.setText("0");
        }
        else {
            commaButton.setEnabled(true);
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
}