package com.example.ultim8calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView display;
    List<Double> numbers = new ArrayList<>();
    List<String> operations = new ArrayList<>();
    int lastIndex = 0;
    double tempResult;
    boolean operationBefore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.txt_display);

    }

    public void onNumberButtonClick(View view) {
        operationBefore = false;
        Button btn = (Button) view;
        display.setText(display.getText().toString() + btn.getText().toString());
    }

    public void onOperationButtonClick(View view) {
        Button btn = (Button) view;
        if (!display.getText().toString().isEmpty() && !display.getText().toString().substring(display.getText().toString().length() - 1).equals("E")) {
            if (operationBefore) {
                display.setText(display.getText().toString().substring(0, display.getText().toString().length() - 1) + btn.getText().toString());
                operations.remove(operations.size() - 1);
                operations.add(btn.getText().toString());
            }
            else {
                operationBefore = true;
                numbers.add(Double.parseDouble(display.getText().toString().substring(lastIndex, display.getText().toString().length()).replace(',', '.')));
                lastIndex = display.getText().toString().length() + 1;
                operations.add(btn.getText().toString());
                display.setText(display.getText().toString() + btn.getText().toString());
            }
        }
    }

    public void onEqualsButtonClick(View view) {
        if (!operationBefore && !display.getText().toString().isEmpty()) {
            numbers.add(Double.parseDouble(display.getText().toString().substring(lastIndex, display.getText().toString().length()).replace(',', '.')));
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
            if (isCommaNecessary(tempResult)) {
                display.setText(String.format(Locale.FRANCE, "%,.8f", tempResult));
            }
            else {
                display.setText(String.valueOf((int) tempResult));
            }
        }
    }

    public void onCommaButtonClick(View view) {
        if (!operationBefore && !display.getText().toString().isEmpty()) {
            display.setText(display.getText() + ",");
        }
    }

    public void onClearButtonClick(View view) {
        if (!display.getText().toString().isEmpty()) {
            String lastChar = display.getText().toString().substring(display.getText().toString().length() - 1);
            if (isNumber(lastChar)) {
                display.setText(display.getText().toString().substring(0, display.getText().toString().length() - 1));
                if (!display.getText().toString().isEmpty() && !isNumber(lastChar) && !lastChar.equals("E")  && !lastChar.equals(".")) {
                    operationBefore = true;
                }
            }
            else {
                if (!operations.isEmpty() && lastChar.equals(operations.get(operations.size() - 1))) {
                    numbers.remove(numbers.size() - 1);
                    operations.remove(operations.size() - 1);
                    if (!operations.isEmpty()) {
                        lastIndex = display.getText().toString().lastIndexOf(operations.get(operations.size() - 1)) + 1;
                    }
                }
                if (operations.isEmpty()) {
                    lastIndex = 0;
                }
                operationBefore = false;
                display.setText(display.getText().toString().substring(0, display.getText().toString().length() - 1));
            }
        }
    }

    public void onClearAllButtonClick(View view) {
        numbers.clear();
        operations.clear();
        lastIndex = 0;
        operationBefore = false;
        display.setText("");
    }

    public boolean isCommaNecessary(double number) {
        int comparedInt = (int) number;
        if ((double) comparedInt == number) {
            return false;
        }
        else {
            return true;
        }
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