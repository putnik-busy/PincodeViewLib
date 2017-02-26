package com.sergey.sample.mvvm.customdependencies;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Sergey Rodionov
 */

public class TypefaceCustomDependency {

    @BindingAdapter("fontName")
    public static void setFont(TextView view, @NonNull String fontName) {
        String fontPath;
        fontPath = "fonts/" + fontName;
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), fontPath);
        view.setTypeface(typeface);
    }

    @BindingAdapter("fontName")
    public static void setFont(EditText view, @NonNull String fontName) {
        String fontPath;
        fontPath = "fonts/" + fontName;
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), fontPath);
        view.setTypeface(typeface);
    }

    @BindingAdapter("fontName")
    public static void setFont(Button view, @NonNull String fontName) {
        String fontPath = "fonts/" + fontName;
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), fontPath);
        view.setTypeface(typeface);
    }
}
