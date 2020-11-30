package com.fclever.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.UUID;

/**
 * Md5加密工具类
 * @author Fclever
 * @create 2020-11-30 12:12
 */
public class Md5Utils {

    /**
     * 生成盐
     * @return
     */
    public static String createSalt(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 生成加密字符串
     * @param source 待加密数据
     * @param salt  盐值
     * @param hashIterations  散列次数
     * @return
     */
    public static String md5(String source, String salt, Integer hashIterations){
        return new Md5Hash(source,salt,hashIterations).toString();
    }
}
