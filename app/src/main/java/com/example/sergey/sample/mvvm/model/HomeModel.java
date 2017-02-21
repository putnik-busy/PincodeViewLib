package com.example.sergey.sample.mvvm.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.sergey.sample.BR;

/**
 * @author Sergey Rodionov
 */

public class HomeModel extends BaseObservable {

    private String mAuthResult;

    @Bindable
    public String getAuthResult() {
        return mAuthResult;
    }

    public void setAuthResult(String authResult) {
        mAuthResult = authResult;
        notifyPropertyChanged(BR.authResult);
    }
}
