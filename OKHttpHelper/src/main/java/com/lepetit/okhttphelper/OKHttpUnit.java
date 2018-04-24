package com.lepetit.okhttphelper;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUnit {
    private OkHttpClient client;
    private static OKHttpUnit instance;

    private OKHttpUnit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new MyCookies());
        client = builder.build();
    }

    private static OKHttpUnit getInstance() {
        if (instance == null) {
            instance = new OKHttpUnit();
        }
        return instance;
    }

    //同步的Get请求
    //return response
    private Response _getSync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return  response;
    }

    //同步的Get请求
    //return string
    private String _getSyncString(String url) throws IOException {
        Response response = _getSync(url);
        return response.body().string();
    }

    //异步的Get请求
    private void _getAsync(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    //同步的Post请求
    //return response
    private Response _postSync(String url, RequestBody body, Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headers)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    //同步的Post请求
    //return string
    private String _postSyncString(String url, RequestBody body, Headers headers) throws IOException {
        Response response = _postSync(url, body, headers);
        return response.body().string();
    }

    //异步的Post请求
    private void _postAsync(String url, RequestBody body, Headers headers, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //外部调用方法
    public static Response getSync(String url) throws IOException {
        return getInstance()._getSync(url);
    }

    public static String getSyncString(String url) throws IOException {
        return getInstance()._getSyncString(url);
    }

    public static void getAsync(String url, Callback callback) {
        getInstance()._getAsync(url, callback);
    }

    public static Response postSync(String url, RequestBody body, Headers headers) throws IOException {
        return getInstance()._postSync(url, body, headers);
    }

    public static String postSyncString(String url, RequestBody body, Headers headers) throws IOException {
        return getInstance()._postSyncString(url, body, headers);
    }

    public static void postAsync(String url, RequestBody body, Headers headers, Callback callback) {
        getInstance()._postAsync(url, body, headers, callback);
    }
}
