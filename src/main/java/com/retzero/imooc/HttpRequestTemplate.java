package com.retzero.imooc;

import com.google.gson.Gson;
import com.retzero.imooc.entity.ClassCollect;
import com.retzero.imooc.entity.ClassDetails;
import com.retzero.imooc.entity.ClassSecondLevel;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * @author JackJun
 * @date 2020/6/22 9:26 下午
 */
public class HttpRequestTemplate {

    private static final Gson gson = new Gson();

    public static List<String> getClassInfo(int mid, int cid) throws IOException {
        String url = "https://coding.imooc.com/lesson/m3u8h5?mid=" + mid + "&cid=" + cid + "&ssl=1&cdn=aliyun1";
        ResponseBody resp = HttpClientHelper.requestByGet(url);
        assert resp != null;
        ClassCollect res = gson.fromJson(resp.string(), ClassCollect.class);
        return IMoocDecoderBackup.toUrls(res.getData().getInfo());
    }

    public static ClassDetails getClassDetails(String url) throws IOException {
        ResponseBody resp = HttpClientHelper.requestByGet(url);
        assert resp != null;
        ClassSecondLevel res = gson.fromJson(resp.string(), ClassSecondLevel.class);
        List<String> allClass = IMoocDecoderBackup.toUrls(res.getData().getInfo());
        String keyUrl = IMoocDecoderBackup.getKeyAddr(res.getData().getInfo());
        resp = HttpClientHelper.requestByGet(keyUrl);
        assert resp != null;
        res = gson.fromJson(resp.string(), ClassSecondLevel.class);
        byte[] key = IMoocDecoderBackup.decoderKey(res.getData().getInfo());
        return ClassDetails.builder().key(key).urls(allClass).build();
    }

    public static byte[] getVideoByte(String url) throws IOException {
        ResponseBody resp = HttpClientHelper.requestByGet(url);
        assert resp != null;
        return resp.bytes();
    }
}
