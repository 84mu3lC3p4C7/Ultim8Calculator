package com.example.ultim8calculator;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import androidx.core.content.res.ResourcesCompat;

import pl.droidsonroids.gif.GifImageView;

public class ComponentVisualsManager {
    Activity activity;
    HorizontalScrollView hsvCalculationLine, hsvInputOutputLine;
    GifImageView background;
    Button changeColorButton;
    int[] primaryButtonsID = {R.id.btn_number0, R.id.btn_number1, R.id.btn_number2, R.id.btn_number3, R.id.btn_number4, R.id.btn_number5, R.id.btn_number6, R.id.btn_number7, R.id.btn_number8, R.id.btn_number9, R.id.btn_comma};
    int[] secondaryButtonsID = {R.id.btn_operation1, R.id.btn_operation2, R.id.btn_operation3, R.id.btn_operation4, R.id.btn_operation5, R.id.btn_operation6, R.id.btn_negation, R.id.btn_bracket1, R.id.btn_bracket2, R.id.btn_delete1, R.id.btn_delete2};
    String primaryButtonsColor, secondaryButtonsColor;

    public ComponentVisualsManager(Activity activity, String[] loadedData) {
        this.activity = activity;
        setupComponentVisualsManager(loadedData);
    }
    private void setupComponentVisualsManager(String[] loadedData) {
        hsvCalculationLine = activity.findViewById(R.id.hsv_calculation);
        hsvInputOutputLine = activity.findViewById(R.id.hsv_inputOutput);
        changeColorButton = activity.findViewById(R.id.btn_number0);


        if (loadedData != null && loadedData.length > 0) {
            try {
                primaryButtonsColor = loadedData[0];
                secondaryButtonsColor = loadedData[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                primaryButtonsColor = "#ffffff";
                secondaryButtonsColor = "#ffffff";
            }
            if (!primaryButtonsColor.equals("null")) {
                updatePrimaryButtonsColor(primaryButtonsColor);
            }
            if (!secondaryButtonsColor.equals("null")) {
                updateSecondaryButtonsColor(secondaryButtonsColor);
            }
        }



        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        background = activity.findViewById(R.id.gif_background);
        background.setRenderEffect(RenderEffect.createBlurEffect(8, 8, Shader.TileMode.MIRROR));
    }

    public void scrollCalculationLineRight() {
        hsvCalculationLine.fullScroll(View.FOCUS_RIGHT);
    }

    public void scrollInputOutputLineRight() {
        hsvInputOutputLine.fullScroll(View.FOCUS_RIGHT);
    }

    public void updatePrimaryButtonsColor(String primaryButtonsColor) {
        System.out.println(primaryButtonsColor + "h");
        this.primaryButtonsColor = primaryButtonsColor;
        Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.btn_enabled1_background, null);
        drawable.setTint(Color.parseColor(primaryButtonsColor));
        Button btn;
        for (Integer id : primaryButtonsID) {
            btn = activity.findViewById(id);
            btn.setBackground(drawable);
        }
    }

    public void updateSecondaryButtonsColor(String secondaryButtonsColor) {
        System.out.println(secondaryButtonsColor);
        this.secondaryButtonsColor = secondaryButtonsColor;
        Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.btn_enabled2_background, null);
        drawable.setTint(Color.parseColor(secondaryButtonsColor));
        Button btn;
        for (Integer id : secondaryButtonsID) {
            btn = activity.findViewById(id);
            btn.setBackground(drawable);
        }
    }

    public String getCVMData() {
        return primaryButtonsColor + ";" + secondaryButtonsColor;
    }
}
