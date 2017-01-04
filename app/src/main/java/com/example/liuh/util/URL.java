package com.example.liuh.util;

/**
 * Created by Administrator on 2016-09-07.
 */
public class URL {

    /**
     * 常用接口、url
     */
    private URL() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 更新地址
     */
    public static final String UpdateURL = "http://192.168.11.131:8080/update.json";

    /**
     * 基础地址
     */
    public static final String BaseURL = "http://192.168.1.104:8080/mobileSafe/";
}
