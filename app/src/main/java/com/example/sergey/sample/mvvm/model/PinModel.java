package com.example.sergey.sample.mvvm.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.example.sergey.sample.BR;


/**
 * @author Sergey Rodionov
 */

public class PinModel extends BaseObservable {

    private String mInputPassword;
    private int mNumberAllCountPinCode;
    private boolean mError;
    private int mNumberAllDrawablePinCode;
    private int mNumberFillDrawablePinCode;

    @Bindable
    public String getInputPassword() {
        return mInputPassword;
    }

    public void setInputPassword(String inputPassword) {
        mInputPassword = inputPassword;
        notifyPropertyChanged(BR.inputPassword);
    }

    @Bindable
    public int getNumberAllCountPinCode() {
        return mNumberAllCountPinCode;
    }

    public void setNumberAllCountPinCode(int numberAllCountPinCode) {
        mNumberAllCountPinCode = numberAllCountPinCode;
        notifyPropertyChanged(BR.numberAllCountPinCode);
    }

    @Bindable
    public boolean isError() {
        return mError;
    }

    public void setError(boolean error) {
        mError = error;
        notifyPropertyChanged(BR.error);
    }

    @Bindable
    public int getNumberAllDrawablePinCode() {
        return mNumberAllDrawablePinCode;
    }

    public void setNumberAllDrawablePinCode(int numberAllDrawablePinCode) {
        mNumberAllDrawablePinCode = numberAllDrawablePinCode;
        notifyPropertyChanged(BR.numberAllDrawablePinCode);
    }

    @Bindable
    public int getNumberFillDrawablePinCode() {
        return mNumberFillDrawablePinCode;
    }

    public void setNumberFillDrawablePinCode(int numberFillDrawablePinCode) {
        mNumberFillDrawablePinCode = numberFillDrawablePinCode;
        notifyPropertyChanged(BR.numberFillDrawablePinCode);
    }
}
