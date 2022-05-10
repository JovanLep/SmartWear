package com.lespayne.smartwear.net;

import static com.lespayne.smartwear.net.RetrofitManager.BASE_URL;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST(BASE_URL + "/fz/login")
    Call<ResponseBody> doLogin(@Body RequestBody body);

    @POST(BASE_URL + "/fz/sign")
    Call<ResponseBody> doRegister(@Body RequestBody body);

    @GET(BASE_URL + "/fz/info")
    Call<ResponseBody> getInfoBack();

    @GET(BASE_URL +  "/fz/person/info_list")
    Call<ResponseBody> getHistoryList(@Query("userId") String userId,
                                      @Query("startTime") String startTime,
                                      @Query("dateTime") String dateTime);
}
