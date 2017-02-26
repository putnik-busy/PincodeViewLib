package com.sergey.sample.mvvm.converters;

import android.databinding.BindingConversion;
import android.view.View;

/**
 * @author Sergey Rodionov
 */
public class BooleanToVisibilityConverter {
    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }
}
