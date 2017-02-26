package com.sergey.sample.mvvm.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.sergey.sample.BR;


/**
 * @author Sergey Rodionov
 */

public class PinCodeModel extends BaseObservable {

    private int mNumberAllDrawablePinCode;
    private int mNumberFillDrawablePinCode;
    private int mNumberSecondFillDrawablePinCode;
    private String mInputPassword;
    private boolean isVisibleSecondLinePassword;
    private String mSecondUserPassword;
    private String mGlobalUserPassword;
    private int mErrorChar = -1;
    private boolean isClearText;
    private boolean isError;

    @Bindable
    public boolean isClearText() {
        return isClearText;
    }

    public void setClearText(boolean clearText) {
        this.isClearText = clearText;
        notifyPropertyChanged(BR.clearText);
    }

    @Bindable
    public int getNumberAllDrawablePinCode() {
        return mNumberAllDrawablePinCode;
    }

    public void setNumberAllDrawablePinCode(int numberAllDrawablePinCode) {
        this.mNumberAllDrawablePinCode = numberAllDrawablePinCode;
        notifyPropertyChanged(BR.numberAllDrawablePinCode);
    }

    @Bindable
    public int getNumberFillDrawablePinCode() {
        return mNumberFillDrawablePinCode;
    }

    public void setNumberFillDrawablePinCode(int numberFillDrawablePinCode) {
        this.mNumberFillDrawablePinCode = numberFillDrawablePinCode;
        notifyPropertyChanged(BR.numberFillDrawablePinCode);
    }

    @Bindable
    public int getNumberSecondFillDrawablePinCode() {
        return mNumberSecondFillDrawablePinCode;
    }

    public void setNumberSecondFillDrawablePinCode(int numberSecondFillDrawablePinCode) {
        this.mNumberSecondFillDrawablePinCode = numberSecondFillDrawablePinCode;
        notifyPropertyChanged(BR.numberSecondFillDrawablePinCode);
    }

    @Bindable
    public String getInputPassword() {
        return mInputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.mInputPassword = inputPassword;
        notifyPropertyChanged(BR.inputPassword);
    }

    @Bindable
    public boolean isVisibleSecondLinePassword() {
        return isVisibleSecondLinePassword;
    }

    public void setVisibleSecondLinePassword(boolean visibleSecondLinePassword) {
        isVisibleSecondLinePassword = visibleSecondLinePassword;
        notifyPropertyChanged(BR.visibleSecondLinePassword);
    }

    @Bindable
    public String getSecondUserPassword() {
        return mSecondUserPassword;
    }

    private void setSecondUserPassword(String secondUserPassword) {
        mSecondUserPassword = secondUserPassword;
        notifyPropertyChanged(BR.secondUserPassword);
    }

    @Bindable
    public String getGlobalUserPassword() {
        return mGlobalUserPassword;
    }

    public void setGlobalUserPassword(String globalUserPassword) {
        if (this.mGlobalUserPassword != null && globalUserPassword != null) {
            if (this.mGlobalUserPassword.length() > globalUserPassword.length()) {
                setErrorChar(-1);
            }
        }
        if (getErrorChar() == -1) {
            this.mGlobalUserPassword = globalUserPassword;

            if (!isVisibleSecondLinePassword() && mGlobalUserPassword.length() <=
                    getNumberAllDrawablePinCode()) {
                setInputPassword(mGlobalUserPassword);
            } else {
                setSecondUserPassword(mGlobalUserPassword);
            }
            notifyPropertyChanged(BR.globalUserPassword);
        }
    }

    @Bindable
    public int getErrorChar() {
        return mErrorChar;
    }

    public void setErrorChar(int errorChar) {
        this.mErrorChar = errorChar;
        notifyPropertyChanged(BR.errorChar);
    }

    @Bindable
    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
        notifyPropertyChanged(BR.error);
    }
}
