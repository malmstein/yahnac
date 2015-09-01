package com.malmstein.yahnac.views;

import android.graphics.Color;

public class ColorTweaker {

    public static final float COMPONENT_MAX = 1f;

    private static final int HSV_BRIGHTNESS = 2;
    private static final float FACTOR_PROGRESSBAR_BRIGHTNESS = 1.4f;
    private static final float FACTOR_STATUSBAR_BRIGHTNESS = 0.9f;

    public int getProgressBarVariantOf(int originalColor) {
        float[] hsv = new float[3];
        int alpha = Color.alpha(originalColor);
        Color.colorToHSV(originalColor, hsv);

        hsv[HSV_BRIGHTNESS] = multiplyComponent(hsv[HSV_BRIGHTNESS], FACTOR_PROGRESSBAR_BRIGHTNESS);

        return Color.HSVToColor(alpha, hsv);
    }

    public int getStatusBarVariantOf(int originalColor) {
        float[] hsv = new float[3];
        int alpha = Color.alpha(originalColor);
        Color.colorToHSV(originalColor, hsv);

        hsv[HSV_BRIGHTNESS] = multiplyComponent(hsv[HSV_BRIGHTNESS], FACTOR_STATUSBAR_BRIGHTNESS);

        return Color.HSVToColor(alpha, hsv);
    }

    private float multiplyComponent(float component, float factor) {
        return Math.min(component * factor, COMPONENT_MAX);
    }

}
