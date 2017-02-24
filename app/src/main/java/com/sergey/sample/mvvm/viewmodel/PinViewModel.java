package com.sergey.sample.mvvm.viewmodel;

import android.content.Intent;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;

import com.sergey.sample.BR;
import com.sergey.sample.databinding.ActivityAuthBinding;
import com.sergey.sample.mvvm.Constants;
import com.sergey.sample.mvvm.activity.SecondActivity;
import com.sergey.sample.mvvm.model.PinModel;

/**
 * @author Sergey Rodionov
 */

public class PinViewModel {
    private AppCompatActivity mActivity;
    private PinModel mModel;
    private boolean mCompleted;

    public PinViewModel(AppCompatActivity activity, ActivityAuthBinding binding) {
        mActivity = activity;
        mModel = binding.getModel();
        mModel.setNumberAllDrawablePinCode(4);
        mModel.setNumberFillDrawablePinCode(0);
        listenerInputPassword(binding);
    }

    private void listenerInputPassword(final ActivityAuthBinding binding) {
        binding.getModel().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.inputPassword) {
                    mModel.setNumberFillDrawablePinCode(mModel.getInputPassword().length());
                    if (!mCompleted && mModel.getInputPassword().length() ==
                            mModel.getNumberAllDrawablePinCode()) {
                        mCompleted = true;
                        changeActivity();
                    }
                }
            }
        });
    }

    private void changeActivity() {
        Intent mainIntent = new Intent(mActivity, SecondActivity.class);
        mainIntent.putExtra(Constants.AUTH_RESULT, "Авторизация прошла успешно");
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(mainIntent);
    }
}
