package com.sergey.sample.mvvm.viewmodel;

import android.content.Intent;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;

import com.sergey.sample.BR;
import com.sergey.sample.databinding.ActivityPinBinding;
import com.sergey.sample.mvvm.Constants;
import com.sergey.sample.mvvm.activity.AuthActivity;
import com.sergey.sample.mvvm.activity.PinCodeActivity;
import com.sergey.sample.mvvm.model.PinCodeModel;

/**
 * @author Sergey Rodionov
 */

public class PinCodeViewModel {
    private AppCompatActivity mActivity;
    private PinCodeModel mModel;
    private boolean isCompleted;
    private boolean isCompletedSecond;

    public PinCodeViewModel(PinCodeActivity activity, ActivityPinBinding binding) {
        mActivity = activity;
        mModel = binding.getModel();
        mModel.setNumberAllDrawablePinCode(4);
        mModel.setNumberFillDrawablePinCode(0);
        listenerFirstLinePassword(binding);
        listenerSecondLinePassword(binding);
    }

    private void listenerFirstLinePassword(final ActivityPinBinding binding) {
        binding.getModel().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.inputPassword) {
                    if (!isCompleted) {
                        mModel.setNumberFillDrawablePinCode(mModel.getInputPassword().length());
                        if (!isCompleted && binding.getModel().getInputPassword().length() ==
                                binding.getModel().getNumberAllDrawablePinCode()) {
                            mModel.setVisibleSecondLinePassword(true);
                            mModel.setClearText(true);
                            isCompleted = true;
                        }
                    }
                }
            }
        });
    }

    private void listenerSecondLinePassword(final ActivityPinBinding binding) {
        binding.getModel().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.secondUserPassword) {
                    if (mModel.getNumberSecondFillDrawablePinCode() == 0 &&
                            (mModel.getSecondUserPassword() == null ||
                                    mModel.getSecondUserPassword().equals(""))) {
                        isCompleted = false;
                        mModel.setClearText(false);
                        mModel.setVisibleSecondLinePassword(false);
                        mModel.setGlobalUserPassword("");
                    } else if (mModel.getErrorChar() == -1 && !isCompletedSecond) {
                        mModel.setNumberSecondFillDrawablePinCode(
                                mModel.getSecondUserPassword().length());
                        int temp = checkPasswordInput(mModel.getInputPassword(),
                                mModel.getSecondUserPassword());
                        if (temp != -1) {
                            mModel.setErrorChar(temp);
                        }
                        if (mModel.getErrorChar() == -1 && !isCompletedSecond &&
                                binding.getModel().getSecondUserPassword().length() ==
                                        binding.getModel().getNumberAllDrawablePinCode()) {
                            isCompletedSecond = true;
                            changeActivity(mModel.getSecondUserPassword());
                        }
                    }
                }
            }
        });
    }

    private int checkPasswordInput(String inputPassword, String secondUserPassword) {
        for (int i = 0; i < secondUserPassword.length(); i++) {
            if (secondUserPassword.charAt(i) != inputPassword.charAt(i)) {
                return i + 1;
            }
        }
        return -1;
    }

    private void changeActivity(String userPassword) {
        Intent mainIntent = new Intent(mActivity, AuthActivity.class);
        mainIntent.putExtra(Constants.PASS_RESULT, userPassword);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(mainIntent);
    }
}
