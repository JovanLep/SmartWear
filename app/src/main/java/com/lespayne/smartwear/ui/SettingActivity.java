package com.lespayne.smartwear.ui;

import android.content.Intent;
import android.text.TextUtils;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.lespayne.base.BaseActivity;
import com.lespayne.smartwear.App;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.adapter.SelfInfoAdapter;
import com.lespayne.smartwear.databinding.ActivitySettingBinding;
import com.lespayne.smartwear.model.SelfBean;
import com.lespayne.smartwear.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    @Override
    protected int bindLayout() {
        return R.layout.activity_setting;
    }

    String[] left_str2 = {"设备名称", "连接状态", "电池电量", "上报状态", "联系客服"};
    String connectStr = App.getApp().mDeviceConnected ? "已连接" : "未连接";
    String state = (App.getApp().isConnect && App.getApp().mDeviceConnected) ? "正常" : "异常";
    String beat = (App.getApp().beat == 0 ? 0 : App.getApp().beat) + "%";
    String[] right_str2 = {TextUtils.isEmpty(UserManager.getInstance().getDeviceId()) ? "无连接设备" : UserManager.getInstance().getDeviceId(),
            connectStr, beat, state, "01223334444"};

    public List<SelfBean> getList2() {
        List<SelfBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SelfBean selfBean = new SelfBean();
            selfBean.setLeftTip(left_str2[i]);
            selfBean.setRightContent(right_str2[i]);
            list.add(selfBean);
        }
        return list;
    }

    @Override
    protected void initEvent() {
        dataBinding.layoutSettingTitle.setTitle("系统设置", this);
        SelfInfoAdapter selfInfoAdapter = new SelfInfoAdapter(this, getList2());
        dataBinding.settingList.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.settingList.setAdapter(selfInfoAdapter);

        dataBinding.activityLoginOut.setOnClickListener(view -> {
            UserManager.getInstance().quickLogin();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}