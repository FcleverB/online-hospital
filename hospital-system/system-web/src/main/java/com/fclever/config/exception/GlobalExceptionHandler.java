package com.fclever.config.exception;

import com.fclever.vo.AjaxResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理
 * @author Fclever
 * @create 2020-10-27 13:54
 */
@RestControllerAdvice // Rest会以JSON的形式返回数据
public class GlobalExceptionHandler {

    /**
     * 方法参数无效异常
     * 当系统出现MethodArgumentNotValidException这个异常时，会调用下面的方法
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AjaxResult jsonErrorHandler(MethodArgumentNotValidException e){
        return AjaxResult.error(e.getMessage());
    }
}
