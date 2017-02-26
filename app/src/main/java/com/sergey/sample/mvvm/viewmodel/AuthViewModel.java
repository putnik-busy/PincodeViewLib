package com.sergey.sample.mvvm.viewmodel;

import android.content.Intent;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;

import com.sergey.sample.BR;
import com.sergey.sample.R;
import com.sergey.sample.databinding.ActivityAuthBinding;
import com.sergey.sample.mvvm.Constants;
import com.sergey.sample.mvvm.activity.ResultAuthActivity;
import com.sergey.sample.mvvm.dialogs.RossMaterialDialog;
import com.sergey.sample.mvvm.model.AuthModel;

/**
 * @author Sergey Rodionov
 */

public class AuthViewModel {
    private AppCompatActivity mActivity;
    private AuthModel mModel;
    private boolean isCompleted;

    public AuthViewModel(AppCompatActivity activity, ActivityAuthBinding binding) {
        mActivity = activity;
        mModel = binding.getModel();
        mModel.setNumberAllDrawablePinCode(4);
        mModel.setNumberFillDrawablePinCode(0);
        Intent intent = activity.getIntent();
        if (intent != null) {
            String passResult = activity.getIntent().getStringExtra(Constants.PASS_RESULT);
            mModel.setUserPassword(passResult);
        }
        listenerInputPassword(binding);
    }

    private void listenerInputPassword(final ActivityAuthBinding binding) {
        binding.getModel().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.inputPassword) {
                    mModel.setNumberFillDrawablePinCode(mModel.getInputPassword().length());
                    if (!isCompleted && mModel.getInputPassword().length() ==
                            mModel.getNumberAllDrawablePinCode()) {
                        isCompleted = true;
                        changeActivity();
                    }
                }
            }
        });
    }

    private void changeActivity() {
        if (mModel.getInputPassword().equals(mModel.getUserPassword())) {
            Intent mainIntent = new Intent(mActivity, ResultAuthActivity.class);
            mainIntent.putExtra(Constants.AUTH_RESULT, "Авторизация прошла успешно");
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mActivity.startActivity(mainIntent);
        } else {
            mModel.setError(true);
            isCompleted = false;
            new RossMaterialDialog.Builder("errorDlg", mActivity.getSupportFragmentManager())
                    .setTitle(R.string.error)
                    .setPositiveButtonText("Ок")
                    .setText("Повторите ввод пароля")
                    .show();
        }
    }
}
