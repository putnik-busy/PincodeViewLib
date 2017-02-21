package com.example.sergey.sample.mvvm.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.sergey.sample.R;
import com.example.sergey.sample.databinding.ActivityHomeBinding;
import com.example.sergey.sample.mvvm.model.HomeModel;
import com.example.sergey.sample.mvvm.viewmodel.HomeViewModel;

/**
 * @author Sergey Rodionov
 */

public class SecondActivity extends AppCompatActivity {
    private ActivityHomeBinding mBinding;
    private HomeModel mModel;
    private HomeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mModel = new HomeModel();
        mBinding.setModel(mModel);
        mViewModel = new HomeViewModel(this, mBinding);
        mBinding.setViewmodel(mViewModel);
    }
}
