package com.example.ultim8calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Calculator calculator;
    DataManager dataManager;
    ComponentVisualsManager cvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = new DataManager(this);
        calculator = new Calculator(this, dataManager.loadData("calculator-data.txt"));
        cvm = new ComponentVisualsManager(this, dataManager.loadData("cvm-data.txt"));

        Intent intent = getIntent();
        if (intent != null) {
            String primaryButtonColor = intent.getStringExtra("primaryButtonsColor");
            if (primaryButtonColor != null && primaryButtonColor.length() == 7) {
                cvm.updatePrimaryButtonsColor(primaryButtonColor);
            }
            String secondaryButtonColor = intent.getStringExtra("secondaryButtonsColor");
            if (secondaryButtonColor != null && secondaryButtonColor.length() == 7) {
                cvm.updateSecondaryButtonsColor(secondaryButtonColor);
            }
            dataManager.saveData(cvm.getCVMData(), "cvm-data.txt");
        }
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
        dataManager.saveData(calculator.getCalculatorData(), "calculator-data.txt");
        super.onPause();
    }

    public void switchToSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}