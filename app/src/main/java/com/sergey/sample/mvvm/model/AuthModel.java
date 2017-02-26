package com.sergey.sample.mvvm.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.sergey.sample.BR;


/**
 * @author Sergey Rodionov
 */

public class AuthModel extends BaseObservable {

    private String mInputPassword;
    private boolean isError;
    private int mNumberAllDrawablePinCode;
    private int mNumberFillDrawablePinCode;
    private String mUserPassword;

    @Bindable
    public String getInputPassword() {
        return mInputPassword;
    }

    public void setInputPassword(String inputPassword) {
        mInputPassword = inputPassword;
        notifyPropertyChanged(BR.inputPassword);
    }

    @Bindable
    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
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

    @Bindable
    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
        notifyPropertyChanged(BR.userPassword);
    }
}
