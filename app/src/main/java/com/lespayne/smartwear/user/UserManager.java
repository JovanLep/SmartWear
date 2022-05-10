package com.lespayne.smartwear.user;

import android.text.TextUtils;

import com.lespayne.smartwear.net.RetrofitManager;

public class UserManager {
    private static final String KEY_MEMBER_ID = "user_Id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_PASS_WORD = "user_pwd";
    private static final String KEY_SEX = "user_sex";
    private static final String KEY_HEADER = "user_header";
    private static final String KEY_PHONE = "user_telephone";
    private static final String KEY_AGE = "user_age";
    private static final String KEY_USER_NAME = "user_username";
    private static final String KEY_TOKEN = "user_token";
    private static final String KEY_LOGIN_STATE = "user_login";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_DEVICE_ADDRESS = "device_address";

    private PreferUtils preferUtils = new PreferUtils();

    private UserManager() {
    }

    private static class SingleTon {
        private static UserManager instance = new UserManager();
    }

    public static UserManager getInstance() {
        return UserManager.SingleTon.instance;
    }

    public void quickLogin() {
        preferUtils.remove(KEY_MEMBER_ID);
        preferUtils.remove(KEY_NAME);
        preferUtils.remove(KEY_PASS_WORD);
        preferUtils.remove(KEY_SEX);
        preferUtils.remove(KEY_HEADER);
        preferUtils.remove(KEY_PHONE);
        preferUtils.remove(KEY_AGE);
        preferUtils.remove(KEY_USER_NAME);
        preferUtils.remove(KEY_TOKEN);
        preferUtils.remove(KEY_LOGIN_STATE);
        preferUtils.remove(KEY_DEVICE_ID);
        preferUtils.remove(KEY_DEVICE_ADDRESS);
    }

    public void saveLogin(UserInfo userBean) {
        if (!TextUtils.isEmpty(userBean.getId())) {
            setId(userBean.getId());
        }
        if (!TextUtils.isEmpty(userBean.getName())) {
            setName(userBean.getName());
        }
        if (!TextUtils.isEmpty(userBean.getPassword())) {
            setPassword(userBean.getPassword());
        }
        if (userBean.getSex() != 0) {
            setSex(userBean.getSex());
        }
        if (!TextUtils.isEmpty(userBean.getHeader())) {
            setHeader(userBean.getHeader());
        }
        if (!TextUtils.isEmpty(userBean.getTelephone())) {
            setTelephone(userBean.getTelephone());
        }
        if (userBean.getAge() != 0) {
            setAge(userBean.getAge());
        }
        if (!TextUtils.isEmpty(userBean.getUsername())) {
            setUsername(userBean.getUsername());
        }
        if (!TextUtils.isEmpty(userBean.getToken())) {
            setToken(userBean.getToken());
        }
        if (!TextUtils.isEmpty(userBean.getDeviceId())) {
            setDeviceId(userBean.getDeviceId());
        }
        if (!TextUtils.isEmpty(userBean.getAddress())) {
            setAddress(userBean.getAddress());
        }
        setLogin(userBean.isLogin());
    }

    public String getId() {
        return preferUtils.get(KEY_MEMBER_ID, "");
    }

    public void setId(String id) {
        preferUtils.put(KEY_MEMBER_ID, id);
    }

    public String getName() {
        return preferUtils.get(KEY_NAME, "");
    }

    public void setName(String name) {
        preferUtils.put(KEY_NAME, name);
    }

    public String getPassword() {
        return preferUtils.get(KEY_PASS_WORD, "");
    }

    public void setPassword(String password) {
        preferUtils.put(KEY_PASS_WORD, password);
    }

    public int getSex() {
        return preferUtils.get(KEY_SEX, 0);
    }

    public void setSex(int sex) {
        preferUtils.put(KEY_SEX, sex);
    }

    public String getHeader() {
        return preferUtils.get(KEY_HEADER, "");
    }

    public void setHeader(String header) {
        preferUtils.put(KEY_HEADER, header);
    }

    public String getTelephone() {
        return preferUtils.get(KEY_PHONE, "");
    }

    public void setTelephone(String telephone) {
        preferUtils.put(KEY_PHONE, telephone);
    }

    public int getAge() {
        return preferUtils.get(KEY_AGE, 0);
    }

    public void setAge(int age) {
        preferUtils.put(KEY_AGE, age);
    }

    public String getUsername() {
        return preferUtils.get(KEY_USER_NAME, "");
    }

    public void setUsername(String username) {
        preferUtils.put(KEY_USER_NAME, username);
    }

    public String getToken() {
        return preferUtils.get(KEY_TOKEN, "");
    }

    public void setToken(String token) {
        preferUtils.put(KEY_TOKEN, token);
    }

    public boolean isLogin() {
        return preferUtils.get(KEY_LOGIN_STATE, false);
    }

    public void setLogin(boolean login) {
        preferUtils.put(KEY_LOGIN_STATE, login);
    }

    public void setDeviceId(String deviceId) {
        preferUtils.put(KEY_DEVICE_ID, deviceId);
    }

    public String getDeviceId() {
        return preferUtils.get(KEY_DEVICE_ID, "");
    }

    public void setAddress(String address) {
        preferUtils.put(KEY_DEVICE_ADDRESS, address);
    }

    public String getAddress() {
        return preferUtils.get(KEY_DEVICE_ADDRESS, "");
    }


}
