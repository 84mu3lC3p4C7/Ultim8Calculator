package com.example.ultim8calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;

public class MainActivity extends AppCompatActivity {
    Calculator calculator;
    ComponentVisualsManager cvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        calculator = new Calculator(this);
        cvm = new ComponentVisualsManager(this);
    }

    public void onNumberButtonClick(View view) {
        calculator.addNumberToInputOutputLine(view);
        cvm.scrollInputOutputLineRight();
    }

    public void onCommaButtonClick(View view) {
        calculator.addCommaToInputOutputLine();
        cvm.scrollInputOutputLineRight();
    }

    public void onOperationButtonClick(View view) {
        calculator.addOperationToCalculationLine(view);
        cvm.scrollCalculationLineRight();
    }

    public void OnNegationOrSquareRootButtonClick(View view) {
        calculator.addNegationOrSquareRoot(view);
    }

    public void onBracketsButtonClick(View view) {
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
}