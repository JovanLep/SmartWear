package com.lespayne.smartwear.net;

import com.lespayne.smartwear.BuildConfig;
import com.lespayne.smartwear.user.UserManager;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    public static final String BASE_URL = "http://82.157.6.212:8900";
    private Retrofit retrofit;

    private RetrofitManager() {
        initClient();
    }

    private static class SingleTon {
        private static RetrofitManager instance = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return SingleTon.instance;
    }

    private void initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor());

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LogInterceptor());
        }

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void doLoginBack(String jsonStr, Callback<ResponseBody> callback) {
        Api request = retrofit.create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<ResponseBody> repos = request.doLogin(body);
        repos.enqueue(callback);
    }

    public void doRegisterBack(String jsonStr, Callback<ResponseBody> callback) {
        Api request = retrofit.create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<ResponseBody> repos = request.doRegister(body);
        repos.enqueue(callback);
    }

    public void getInfoBack(Callback<ResponseBody> callback) {
        Api request = retrofit.create(Api.class);
        Call<ResponseBody> repos = request.getInfoBack();
        repos.enqueue(callback);
    }

    public void getHistoryList(Callback<ResponseBody> callback, String startTime, String dateTime) {
        Api request = retrofit.create(Api.class);
        Call<ResponseBody> repos = request.getHistoryList(UserManager.getInstance().getId(), startTime, dateTime);
        repos.enqueue(callback);
    }
}
