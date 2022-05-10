package com.lespayne.smartwear.net;

import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.lespayne.smartwear.user.UserInfo;
import com.lespayne.smartwear.user.UserManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (!TextUtils.isEmpty(UserManager.getInstance().getToken())) {
            String token = UserManager.getInstance().getToken();
            builder.addHeader("token", token);
        }
        return chain.proceed(builder.build());
    }
}
