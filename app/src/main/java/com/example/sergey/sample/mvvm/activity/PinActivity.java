package com.example.sergey.sample.mvvm.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.sergey.sample.R;
import com.example.sergey.sample.databinding.ActivityAuthBinding;
import com.example.sergey.sample.mvvm.model.PinModel;
import com.example.sergey.sample.mvvm.viewmodel.PinViewModel;

public class PinActivity extends AppCompatActivity {
    private ActivityAuthBinding mBinding;
    private PinModel mModel;
    private PinViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        mModel = new PinModel();
        mBinding.setModel(mModel);
        mViewModel = new PinViewModel(this, mBinding);
        mBinding.setViewmodel(mViewModel);
    }
}
