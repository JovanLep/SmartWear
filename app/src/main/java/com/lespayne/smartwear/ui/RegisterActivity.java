package com.lespayne.smartwear.ui;

import android.text.TextUtils;
import com.lespayne.base.BaseActivity;
import com.lespayne.base.utils.Md5Utils;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.dataHelper.GsonUtil;
import com.lespayne.smartwear.databinding.ActivityRegisterBinding;
import com.lespayne.smartwear.model.RegisterJsonBean;
import com.lespayne.smartwear.model.RegisterResultBean;
import com.lespayne.smartwear.net.RetrofitManager;
import com.lespayne.smartwear.user.UserInfo;
import com.lespayne.smartwear.user.UserManager;
import java.io.IOException;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    @Override
    protected int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initEvent() {
        dataBinding.layoutTitle.setTitle("注册用户");

        dataBinding.cbBoy.setOnCheckedChangeListener((buttonView, isChecked) -> dataBinding.cbGril.setChecked(!isChecked));
        dataBinding.cbGril.setOnCheckedChangeListener((buttonView, isChecked) -> dataBinding.cbBoy.setChecked(!isChecked));

        dataBinding.activityRegister.setOnClickListener(v -> {
            if (TextUtils.isEmpty(dataBinding.rightName.getText())) {
                showToast("用户名称不能为空！");
                return;
            }
            if (!dataBinding.cbBoy.isChecked() && !dataBinding.cbGril.isChecked()) {
                showToast("您还没有选择性别！");
                return;
            }
            if (TextUtils.isEmpty(dataBinding.rightPhone.getText())) {
                showToast("手机号不能为空！");
                return;
            }
            if (TextUtils.isEmpty(dataBinding.rightAge.getText())) {
                showToast("年龄不能为空！");
                return;
            }
            if (TextUtils.isEmpty(dataBinding.rightPassword1.getText()) || TextUtils.isEmpty(dataBinding.rightPassword2.getText())) {
                showToast("密码不能为空！");
                return;
            }

            int age =Integer.parseInt(Objects.requireNonNull(dataBinding.rightAge.getText()).toString());
            String pwd = dataBinding.rightPassword1.getText().toString().trim();
            String md5 = Md5Utils.getMD5(pwd);
            RegisterJsonBean registerJsonBean=new RegisterJsonBean(
                    Objects.requireNonNull(dataBinding.rightName.getText()).toString().trim(),
                    dataBinding.cbBoy.isChecked()?1:0,
                    Objects.requireNonNull(dataBinding.rightPhone.getText()).toString().trim(),
                    age,
                    "default",
                    md5,
                    dataBinding.rightName.getText().toString().trim()
            );
            String registerJson = GsonUtil.objectToJson(registerJsonBean);
            RetrofitManager.getInstance().doRegisterBack(registerJson,new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String string = response.body().string();
                        RegisterResultBean baseResult = GsonUtil.jsonToObject(string, RegisterResultBean.class);

                        if (baseResult.getCode() == 10200) {
                            RegisterResultBean.DataBean data = baseResult.getData();
                            UserInfo userInfo = new UserInfo();
                            userInfo.setLogin(true);
                            userInfo.setId(data.getId());
                            userInfo.setName(data.getName());
                            userInfo.setPassword(md5);
                            userInfo.setSex(data.getSex());
                            userInfo.setHeader(data.getHeader());
                            userInfo.setTelephone(data.getTelephone());
                            userInfo.setAge(data.getAge());
                            userInfo.setUsername(data.getUsername());
                            userInfo.setToken(data.getToken().replace("\r|\n", ""));
                            UserManager.getInstance().saveLogin(userInfo);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startActivity(MainActivity.class);
                            finish();
                        }else {
                            showToast("注册失败！");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        });
    }
}
