package com.lespayne.smartwear.ui;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.lespayne.base.BaseActivity;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.adapter.SelfInfoAdapter;
import com.lespayne.smartwear.dataHelper.GsonUtil;
import com.lespayne.smartwear.databinding.ActivityUserInfoBinding;
import com.lespayne.smartwear.model.RegisterResultBean;
import com.lespayne.smartwear.model.SelfBean;
import com.lespayne.smartwear.net.RetrofitManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> {
    private SelfInfoAdapter selfInfoAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_user_info;
    }

     String[] left_str = {"用户id", "用户姓名", "性别", "年龄", "身高", "体重", "手机号"};

    @Override
    protected void initEvent() {
        dataBinding.layoutTitle.setTitle("个人信息", this);
        selfInfoAdapter = new SelfInfoAdapter(this, new ArrayList<>());
        dataBinding.selfList.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.selfList.setAdapter(selfInfoAdapter);

        RetrofitManager.getInstance().getInfoBack(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    RegisterResultBean registerResultBean = GsonUtil.jsonToObject(string, RegisterResultBean.class);
                    RegisterResultBean.DataBean data = registerResultBean.getData();
                    String[] right_str = {data.getId(), data.getName(), data.getSex()+"", data.getAge()+"",
                            "175", "60", data.getTelephone()};
                    List<SelfBean> list = new ArrayList<>();
                    for (int i = 0; i < 7; i++) {
                        SelfBean selfBean = new SelfBean();
                        selfBean.setSex(left_str[i].equals("性别"));
                        selfBean.setLeftTip(left_str[i]);
                        selfBean.setRightContent(right_str[i]);
                        list.add(selfBean);
                    }
                    selfInfoAdapter.setDataList(list);
                    selfInfoAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log("   --" + t.getMessage());
            }
        });
    }
}