package com.example.ultim8calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    Calculator calculator;

    DataManager dataManager;
    ComponentVisualsManager cvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = new DataManager(this);
        calculator = new Calculator(this, dataManager.loadData());
        cvm = new ComponentVisualsManager(this);

    }

    public void onNumberButtonClick(View view) {
        if (!calculator.isOutputNumber) {
            calculator.fixNonNumericOutput();
        }
        calculator.addNumberToInputOutputLine(view);
        cvm.scrollInputOutputLineRight();
    }

    public void onNegationButtonClick(View view) {
        if (!calculator.isOutputNumber) {
            calculator.fixNonNumericOutput();
        }
        calculator.negation();
    }

    public void onCommaButtonClick(View view) {
        if (!calculator.isOutputNumber) {
            calculator.fixNonNumericOutput();
        }
        calculator.addCommaToInputOutputLine();
        cvm.scrollInputOutputLineRight();
    }

    public void onSquareRootButtonClick(View view) {
        if (!calculator.isOutputNumber) {
            calculator.fixNonNumericOutput();
        }
        calculator.addSquareRoot();
    }

    public void onOperationButtonClick(View view) {
        if (!calculator.isOutputNumber) {
            calculator.fixNonNumericOutput();
        }
        calculator.addOperationToCalculationLine(view);
        cvm.scrollCalculationLineRight();
    }

    public void onBracketsButtonClick(View view) {
        if (!calculator.isOutputNumber) {
            calculator.fixNonNumericOutput();
        }
        calculator.addBracketToCalculationLine(view);
        cvm.scrollCalculationLineRight();
    }

    public void onClearButtonClick(View view) {
        calculator.backspace();
    }

    public void onClearAllButtonClick(View view) {
        calculator.clearEverything();
    }

    public void onEqualsButtonClick(View view) {
        calculator.finishCalculation();
        cvm.scrollCalculationLineRight();
    }

    @Override
    protected void onPause() {
        dataManager.saveData(calculator.getCalculatorData());
        super.onPause();
    }
}