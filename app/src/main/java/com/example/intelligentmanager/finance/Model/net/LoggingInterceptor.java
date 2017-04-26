package com.example.intelligentmanager.finance.Model.net;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
/**
 * Created by 聂敏萍 on 2017/3/13.
 */

public class LoggingInterceptor implements Interceptor{
    private static final String TAG = "OkHttp";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response;
    }
}
