package com.lespayne.smartwear.net;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        ResponseBody responseBody = response.body();
        MediaType mediaType = responseBody.contentType();
        String content = responseBody.string();
        String httpMessage = "\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4DHTTP请求\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D" + "\n" +
                "request header:" + "\n" + request.headers() +
                "request url:" + request.url() + (TextUtils.isEmpty(bodyToString(request)) ? "" : ("?" + bodyToString(request))) + "\n" +
                "response time:" + (t2 - t1) / 1e6d + "ms" + "\n" +
                "response body:" + content;
        Log.d("HttpRequest", httpMessage);

        return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
    }


    private String bodyToString(final Request request) {
        try {
            RequestBody requestBody = request.newBuilder().build().body();
            Buffer buffer = new Buffer();
            if (requestBody != null) {
                requestBody.writeTo(buffer);
            }
            return URLDecoder.decode(buffer.readUtf8());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
