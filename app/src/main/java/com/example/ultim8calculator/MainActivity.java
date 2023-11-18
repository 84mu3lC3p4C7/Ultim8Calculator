package com.example.ultim8calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView display;
    NumberFormat decimalFormat = DecimalFormat.getInstance();
    List<Double> numbers = new ArrayList<>();
    List<String> operations = new ArrayList<>();
    String displayText; // just to get rid of these warnings: Do not concatenate text displayed with setText. Use resource string with placeholders.
    double tempResult;
    int lastIndex = 0;
    boolean operationBefore = false;
    boolean commaUsed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.txt_display);

        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setMaximumFractionDigits(8);
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        displayText = display.getText().toString();

    }

    private String getDisplayText() {
        return display.getText().toString();
    }

    public void onNumberButtonClick(View view) {
        operationBefore = false;
        Button btn = (Button) view;
        displayText = getDisplayText() + btn.getText().toString();
        display.setText(displayText);
    }

    public void onOperationButtonClick(View view) {
        Button btn = (Button) view;
        if (!getDisplayText().isEmpty() && !getDisplayText().substring(getDisplayText().length() - 1).equals("E")) {
            if (operationBefore) {
                displayText = getDisplayText().substring(0, getDisplayText().length() - 1) + btn.getText().toString();
                display.setText(displayText);
                operations.set(operations.size() - 1, btn.getText().toString());
            }
            else {
                operationBefore = true;
                commaUsed = false;
                numbers.add(Double.parseDouble(getDisplayText().substring(lastIndex).replace(',', '.')));
                lastIndex = getDisplayText().length() + 1;
                operations.add(btn.getText().toString());
                displayText = getDisplayText() + btn.getText().toString();
                display.setText(displayText);
            }
        }
    }

    public void onEqualsButtonClick(View view) {
        if (!operationBefore && !getDisplayText().isEmpty()) {
            numbers.add(Double.parseDouble(getDisplayText().substring(lastIndex).replace(',', '.')));
            for (int i = 0; i < operations.size(); i++) {
                if ("^".equals(operations.get(i))) {
                    numbers.set(i + 1, Math.pow(numbers.get(i), numbers.get(i + 1)));
                    numbers.remove(i);
                    operations.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < operations.size(); i++) {
                switch (operations.get(i)) {
                    case ("×"):
                        numbers.set(i + 1, numbers.get(i) * numbers.get(i + 1));
                        numbers.remove(i);
                        operations.remove(i);
                        i--;
                        break;
                    case ("÷"):
                        numbers.set(i + 1, numbers.get(i) / numbers.get(i + 1));
                        numbers.remove(i);
                        operations.remove(i);
                        i--;
                        break;
                }
            }
            tempResult = numbers.get(0);
            numbers.remove(0);
            for (int i = 0; i < operations.size(); i++) {
                switch (operations.get(i)) {
                    case ("+"):
                        tempResult += numbers.get(i);
                        numbers.remove(i);
                        operations.remove(i);
                        i--;
                        break;
                    case ("-"):
                        tempResult -= numbers.get(i);
                        numbers.remove(i);
                        operations.remove(i);
                        i--;
                        break;
                }
            }
            lastIndex = 0;
            operationBefore = false;
            commaUsed = false;
            if (isCommaNecessary(tempResult)) {
                display.setText(decimalFormat.format(tempResult).replace('.', ','));
            }
            else {
                display.setText(String.valueOf((int) tempResult));
            }
        }
    }

    public void onCommaButtonClick(View view) {
        if (!commaUsed && !getDisplayText().isEmpty() && !operationBefore) {
            commaUsed = true;
            displayText = display.getText() + ",";
            display.setText(displayText);
        }
    }

    public void onClearButtonClick(View view) {
        if (!getDisplayText().isEmpty()) {
            String lastChar = getDisplayText().substring(getDisplayText().length() - 1);
            if (isNumber(lastChar)) {
                display.setText(getDisplayText().substring(0, getDisplayText().length() - 1));
                if (!getDisplayText().isEmpty() && getDisplayText().substring(getDisplayText().length() - 1).equals(operations.get(operations.size() - 1))) {
                    operationBefore = true;
                }
            }
            else {
                if (!operations.isEmpty() && lastChar.equals(operations.get(operations.size() - 1))) {
                    numbers.remove(numbers.size() - 1);
                    operations.remove(operations.size() - 1);
                    if (operations.isEmpty()) {
                        lastIndex = 0;
                    }
                    else {
                        lastIndex = getDisplayText().lastIndexOf(operations.get(operations.size() - 1)) + 1;
                    }
                }
                operationBefore = false;
                display.setText(getDisplayText().substring(0, getDisplayText().length() - 1));
                if (getDisplayText().substring(getDisplayText().length() - 1).equals(",")) {
                    commaUsed = true;
                }
            }
        }
    }

    public void onClearAllButtonClick(View view) {
        numbers.clear();
        operations.clear();
        lastIndex = 0;
        operationBefore = false;
        commaUsed = false;
        display.setText("");
    }

    public boolean isCommaNecessary(double number) {
        int comparedInt = (int) number;
        return (double) comparedInt != number;
    }

    public boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}