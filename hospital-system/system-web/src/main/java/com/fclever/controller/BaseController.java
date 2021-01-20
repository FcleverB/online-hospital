package com.fclever.controller;

import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;

/**
 * 基础控制层，由服务提供者进行继承，统一规范熔断时的处理
 * @author Fclever
 * @create 2020-12-12 21:02
 */
@DefaultProperties(defaultFallback = "fallback")
public class BaseController {

    /**
     * 远程服务不可用或者出现异常进行回调的方法
     * @return
     */
    public AjaxResult fallback() {
        return AjaxResult.fail("服务器内部异常，请联系管理员！");
    }
}
