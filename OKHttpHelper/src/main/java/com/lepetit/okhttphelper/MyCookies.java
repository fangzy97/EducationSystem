package com.lepetit.okhttphelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookies implements CookieJar {

    private HashMap<String, List<Cookie>> cookiesStore;

    public MyCookies(){
        this.cookiesStore = new HashMap<>();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookiesStore.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> list = cookiesStore.get(url.host());
        return list != null ? list : new ArrayList<Cookie>();
    }
}
