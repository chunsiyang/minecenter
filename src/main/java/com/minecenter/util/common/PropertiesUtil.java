package com.minecenter.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Properties工具
 * @author chunsiyang
 * @date 2018/8/31 17:29
 */
public class PropertiesUtil {

    /**
     * Logger
     */
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * PROP
     */
    private static final Properties PROP = new Properties();

    /**
     * 读取配置文件
     * @param fileName 配置文件名
     * @author chunsiyang
     * @date 2018/8/31 17:29
     */
    public static void readProperties(String fileName){
        try (InputStream in = PropertiesUtil.class.getResourceAsStream("/" + fileName)) {
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            PROP.load(bf);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 根据key读取对应的value
     * @param key 键值
     * @return java.lang.String
     * @author chunsiyang
     * @date 2018/8/31 17:29
     */
    public static String getProperty(String key){
        return PROP.getProperty(key);
    }
}
