package com.example.sergey.sample.mvvm.viewmodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.sergey.sample.databinding.ActivityHomeBinding;
import com.example.sergey.sample.mvvm.Constants;
import com.example.sergey.sample.mvvm.activity.SecondActivity;
import com.example.sergey.sample.mvvm.model.HomeModel;

/**
 * @author Sergey Rodionov
 */

public class HomeViewModel {
    private AppCompatActivity mActivity;
    private HomeModel mModel;

    public HomeViewModel(SecondActivity activity, ActivityHomeBinding binding) {
        mActivity = activity;
        mModel = binding.getModel();
        Intent intent = activity.getIntent();
        if (intent != null) {
            String authResult = activity.getIntent().getStringExtra(Constants.AUTH_RESULT);
            mModel.setAuthResult(authResult);
        }
    }
}
