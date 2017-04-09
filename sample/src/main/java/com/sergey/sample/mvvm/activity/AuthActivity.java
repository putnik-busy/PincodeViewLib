package com.sergey.sample.mvvm.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sergey.sample.R;
import com.sergey.sample.databinding.ActivityAuthBinding;
import com.sergey.sample.mvvm.model.AuthModel;
import com.sergey.sample.mvvm.viewmodel.AuthViewModel;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding mBinding;
    private AuthModel mModel;
    private AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        mModel = new AuthModel();
        mBinding.setModel(mModel);
        mViewModel = new AuthViewModel(this, mBinding);
        mBinding.setViewmodel(mViewModel);
    }
}
