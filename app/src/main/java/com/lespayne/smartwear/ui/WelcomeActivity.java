package com.lespayne.smartwear.ui;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.view.View;

import com.lespayne.base.BaseActivity;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.databinding.ActivityWelcomeBinding;
import com.lespayne.smartwear.user.UserManager;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {
    private final CountDownTimer timer = new CountDownTimer(2200, 1000) {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long l) {
            dataBinding.skip.setText("跳过" + (l / 1000));
        }

        @Override
        public void onFinish() {
            dataBinding.skip.setVisibility(View.GONE);
            doNext();
        }
    };

    @Override
    protected void initEvent() {
        timer.start();
    }

    private void doNext() {
        if (UserManager.getInstance().isLogin()) {
            startActivity(MainActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
        finish();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_welcome;
    }
}