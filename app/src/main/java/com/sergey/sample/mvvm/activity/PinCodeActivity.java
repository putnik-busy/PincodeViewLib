package com.sergey.sample.mvvm.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sergey.sample.R;
import com.sergey.sample.databinding.ActivityPinBinding;
import com.sergey.sample.mvvm.model.PinCodeModel;
import com.sergey.sample.mvvm.viewmodel.PinCodeViewModel;

/**
 * @author Sergey Rodionov
 */

public class PinCodeActivity extends AppCompatActivity {
    private ActivityPinBinding mBinding;
    private PinCodeModel mModel;
    private PinCodeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pin);
        mModel = new PinCodeModel();
        mBinding.setModel(mModel);
        mViewModel = new PinCodeViewModel(this, mBinding);
        mBinding.setViewmodel(mViewModel);
    }
}
