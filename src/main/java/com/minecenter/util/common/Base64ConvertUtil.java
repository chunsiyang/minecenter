package com.minecenter.util.common;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64工具
 *
 * @author chunsiyang
 * @date 2018/8/21 15:14
 */
public class Base64ConvertUtil {

    /**
     * 加密JDK1.8
     *
     * @param str 待加密文本
     * @return java.lang.String
     * @author chunsiyang
     * @date 2018/8/21 15:28
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    /**
     * 解密JDK1.8
     *
     * @param str 待解密文本
     * @return java.lang.String
     * @author chunsiyang
     * @date 2018/8/21 15:28
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }

}
