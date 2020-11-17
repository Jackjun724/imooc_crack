package com.retzero.imooc;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * @author JackJun
 * @date 2020/6/22 8:44 下午
 */
public class AESCBCDecrypt {
    private static final String AES = "AES";

    private static final String AES_CBC = "AES/CBC/PKCS5PADDING";

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     */
    public static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC);
            cipher.init(mode, new SecretKeySpec(key, AES), new IvParameterSpec(iv));
            cipher.update(input);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("加解密出现了异常，当前的key：" + Arrays.toString(key) + "，IV：" + Arrays.toString(iv));
        }
    }
}
