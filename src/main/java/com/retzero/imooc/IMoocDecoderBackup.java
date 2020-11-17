package com.retzero.imooc;

import com.google.gson.Gson;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JackJun
 * @date 2020/6/22 8:44 下午
 */
public class IMoocDecoderBackup {
    /**
     * JavaScript解码算法执行器
     */
    private static Invocable invocable;

    /* 读取JS文件，并构造执行器 */
    static {
        InputStream inputStream = IMoocDecoderBackup.class.getClassLoader().getResourceAsStream("attachment/decode.js");
        if (inputStream != null) {
            try {
                String javaScript = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
                engine.eval(javaScript);
                invocable = (Invocable) engine;
            } catch (ScriptException ignored) {
            }
        }
    }

    /**
     * 解码成字符串
     *
     * @param crypto 待解码的字符串
     * @return 解码后的字符串
     */
    public static String decoderString(String crypto) {
        return invoke("decode2String", crypto);
    }

    /**
     * 获取慕课网视频的密匙key
     *
     * @param crypto 待解码的字符串
     * @return 字节数组
     */
    public static byte[] decoderKey(String crypto) {
        @SuppressWarnings("unchecked")
        Map<String, Double> obj = (Map<String, Double>) new Gson().fromJson(invoke("decode2Bytes", crypto), Map.class);
        StringBuilder strRes = new StringBuilder("[");
        for (int i = 0; i < 16; i++) {
            strRes.append(obj.get(String.valueOf(i)));
            if (i != 15) {
                strRes.append(",");
            } else {
                strRes.append("]");
            }
        }

        return new Gson().fromJson(strRes.toString(), byte[].class);
    }

    /**
     * 调用JavaScript方法
     *
     * @param methodName   方法名
     * @param encodeString 待解码的字符串
     * @return 解码后的结果
     */
    private static String invoke(String methodName, String encodeString) {
        try {
            return invocable.invokeFunction(methodName, encodeString).toString();
        } catch (ScriptException | NoSuchMethodException ignored) {
        }
        return null;
    }

    public static List<String> toUrls(String encodedData) {
        String[] resStr = IMoocDecoderBackup.decoderString(encodedData).split("\n");
        return Arrays.stream(resStr).filter(item -> item.indexOf("https://") == 0).collect(Collectors.toList());
    }

    public static String getKeyAddr(String encodedData) {
        String[] resStr = IMoocDecoderBackup.decoderString(encodedData).split("\n");
        String keyStr = Arrays.stream(resStr).filter(item -> item.indexOf("#EXT-X-KEY:METHOD=AES-128") == 0).findFirst().get();
        return keyStr.substring(keyStr.indexOf("https://"), keyStr.length() - 1);
    }
}
