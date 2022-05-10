package com.lespayne.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {
    private static final String TAG = "123654";
    protected DB dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, bindLayout());
        initView();
        initEvent();
    }

    //初始化
    protected abstract int bindLayout();

    protected abstract void initEvent();

    protected void initView() {
    }


    public void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void log(String msg) {
        Log.e(TAG, msg);
    }
}
