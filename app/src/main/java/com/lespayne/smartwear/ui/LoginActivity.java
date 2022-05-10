package com.lespayne.smartwear.ui;
import android.view.inputmethod.EditorInfo;
import com.lespayne.base.BaseActivity;
import com.lespayne.base.utils.Md5Utils;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.dataHelper.GsonUtil;
import com.lespayne.smartwear.databinding.ActivityLoginBinding;
import com.lespayne.smartwear.model.LoginBean;
import com.lespayne.smartwear.model.RegisterResultBean;
import com.lespayne.smartwear.net.RetrofitManager;
import com.lespayne.smartwear.user.UserInfo;
import com.lespayne.smartwear.user.UserManager;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEvent() {
        dataBinding.layoutLoginTitle.setTitle("登录", this);

        dataBinding.isShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                dataBinding.loginPassEdit.setInputType(0x90);
            } else {
                dataBinding.loginPassEdit.setInputType(0x81);
            }
        });

        dataBinding.activityLogin.setOnClickListener(view -> {
            clickLogin();
        });

        dataBinding.loginPassEdit.setOnEditorActionListener((v, actionId, event) -> {
            /*判断是否是“done”键*/
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (dataBinding.activityLogin.isEnabled()) {
                    clickLogin();
                    return true;
                }
                return false;
            }
            return false;
        });

        dataBinding.loginToRegister.setOnClickListener(v -> startActivity(RegisterActivity.class));
    }

    private void clickLogin() {
        String userName = dataBinding.loginUserEdit.getText().toString();
        String pwd = dataBinding.loginPassEdit.getText().toString();
        if ("".equals(userName.trim())) {
            showToast("用户名不能为空！");
            return;
        } else if ("".equals(pwd.trim())) {
            showToast("密码不能为空！");
            return;
        }
        String md5 = Md5Utils.getMD5(pwd);
        String json = GsonUtil.objectToJson(new LoginBean(userName, md5));

        RetrofitManager.getInstance().doLoginBack(json, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
                    RegisterResultBean baseResult = GsonUtil.jsonToObject(str, RegisterResultBean.class);
                    if (baseResult.getCode() == 10200) {
                        RegisterResultBean.DataBean data = baseResult.getData();
                        UserInfo userInfo = new UserInfo();
                        userInfo.setLogin(true);
                        userInfo.setId(data.getId());
                        userInfo.setName(data.getName());
                        userInfo.setPassword(pwd);
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
                    }
                } catch (IOException e) {
                    showToast("登录失败，请重试！");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("登录失败，请重试！");
            }
        });
    }
}