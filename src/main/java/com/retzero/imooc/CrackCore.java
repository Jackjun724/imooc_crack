package com.retzero.imooc;

import com.google.gson.Gson;
import com.retzero.imooc.entity.ClassDetails;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author JackJun
 * @date 2020/6/22 8:43 下午
 */
@Slf4j
public class CrackCore {

    public static void main(String[] args) {

        // JavaScript 脚本，汇聚出 {name,videoId} 的数据
        //copy(resp.data.flatMap(item=>item.media_list.filter(i=> i.media_type==='1').map(i=>{return {name: item.chapter_seqid+'-'+ i.media_seqid +'-'+i.media_name,id: parseInt(i.media_id)}})))
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> clazz = new Gson().fromJson("[" +
                "    {\n" +
                "      \"name\": \"18-1-Flutter升级与适配指南\",\n" +
                "      \"id\": 25071\n" +
                "    }\n" +
                "  ]", List.class);


        // 多线程下载视频
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        for (Map<String, Object> entry : clazz) {
            threadPoolExecutor.execute(() -> {
                String filename = String.valueOf(entry.get("name"));

                try {

                    List<String> urls = HttpRequestTemplate.getClassInfo(Double.valueOf(String.valueOf(entry.get("id"))).intValue(), 321);
                    List<ClassDetails> classDetails = new ArrayList<>();
                    for (String url : urls) {
                        classDetails.add(HttpRequestTemplate.getClassDetails(url));
                    }
                    List<byte[]> mp4 = new ArrayList<>();
                    for (ClassDetails classDetail : classDetails) {
                        List<String> classUrls = classDetail.getUrls();
                        byte[] key = classDetail.getKey();

                        for (int i = 0; i < classUrls.size(); i++) {
                            byte[] byteData = HttpRequestTemplate.getVideoByte(classUrls.get(i));
                            byte[] blockFile = AESCBCDecrypt.aes(byteData, key, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) i}, Cipher.DECRYPT_MODE);
                            mp4.add(blockFile);
                        }
                    }
                    write(filename, mp4);
                    log.info("下载课程完成！{}", filename);
                } catch (Exception e) {
                    log.error("下载课程出错！" + filename, e);
                }
            });
        }


    }

    public static void write(String filename, List<byte[]> data) throws IOException {
        // TODO edit Filepath
        File outputFile = new File("/Users/jackjun/Desktop/video/" + filename + ".m3u8");
        if (!outputFile.exists()) {
            if (!outputFile.createNewFile()) {
                log.error("创建文件失败");
            }
        }
        FileOutputStream outputFileStream = null;

        try {
            outputFileStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (byte[] datum : data) {
            if (outputFileStream != null) {
                outputFileStream.write(datum);
                outputFileStream.flush();
            }
        }

        //close file stream
        try {
            if (outputFileStream != null) {
                outputFileStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
