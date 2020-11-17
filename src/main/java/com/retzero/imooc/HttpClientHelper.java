package com.retzero.imooc;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

/**
 * @author JackJun
 * @date 2020/2/24 8:55 下午
 */
@Slf4j
public class HttpClientHelper {
    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .build();

    // TODO set imooc Cookie
    private final static String COOKIE = "";

    public static ResponseBody requestByGet(String url) {
        try {
            Request request = new Request.Builder().get()
                    .header("Cookie", COOKIE)
                    .url(url).build();
            Call call = HTTP_CLIENT.newCall(request);
            Response resp = call.execute();
            return resp.body();
        }catch (Exception e) {
           log.error("Get Error", e);
        }
        return null;
    }

}
