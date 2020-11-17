package com.fclever.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求相关工具类
 *      获取请求request，响应response，请求参数requestAttribute
 * @author Fclever
 * @create 2020-11-16 14:11
 */
public class ServletUtils {

    /**
     * ServletRequestAttributes获取
     * @return ServletRequestAttributes
     */
    public static ServletRequestAttributes getRequestAttributes(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes)requestAttributes;
    }

    /**
     * 获取Request对象
     * @return Request对象
     */
    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取Response对象
     * @return Response对象
     */
    public static HttpServletResponse getResponse(){
        return getRequestAttributes().getResponse();
    }
}
