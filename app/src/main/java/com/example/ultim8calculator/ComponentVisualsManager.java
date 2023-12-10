package com.example.ultim8calculator;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.view.View;
import android.widget.HorizontalScrollView;

import pl.droidsonroids.gif.GifImageView;

public class ComponentVisualsManager {
    Activity activity;
    HorizontalScrollView hsvCalculationLine, hsvInputOutputLine;
    GifImageView background;
    public ComponentVisualsManager(Activity activity) {
        this.activity = activity;
        setupComponentVisualsManager();
    }

    private void setupComponentVisualsManager() {
        hsvCalculationLine = activity.findViewById(R.id.hsv_calculation);
        hsvInputOutputLine = activity.findViewById(R.id.hsv_inputOutput);
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
}
