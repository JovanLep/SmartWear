package com.lespayne.smartwear.dataHelper;
import androidx.annotation.NonNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetClient {
    private static NetClient netClient;

    private NetClient() {
        client = initOkHttpClient();
    }

    public final OkHttpClient client;

    private OkHttpClient initOkHttpClient() {
        //初始化的时候可以自定义一些参数
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)//设置读取超时为10秒
                .connectTimeout(10000, TimeUnit.MILLISECONDS)//设置链接超时为10秒
                .build();
        return okHttpClient;
    }

    public static NetClient getNetClient() {
        if (netClient == null) {
            netClient = new NetClient();
        }
        return netClient;
    }

    public void callNet(String url, MyCallBack callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(-1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                callback.onResponse(response.body().string());
            }
        });
    }

//    public void callNet2(String url, MyCallBack callback) {
//        String token = UserManager.getInstance().getToken();
//
//        Request request = new Request.Builder().url(url)
//                .addHeader("token",token)
//                .build();
//        Call call = getNetClient().initOkHttpClient().newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.onFailure(-1);
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                callback.onResponse(response.body().string());
//            }
//        });
//    }


    public interface MyCallBack {
        //链接成功执行的方法
        void onFailure(int code);//方法参数用int数据类型，相当于是一个标识

        //链接失败执行的方法
        void onResponse(String json);//方法参数根据个人需求写，可以是字符串，也可以是输入流
    }
}