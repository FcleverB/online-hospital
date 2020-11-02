package com.fclever.config.exception;

import com.fclever.vo.AjaxResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        List<Map<String,Object>> list = new ArrayList<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        for (ObjectError allError : allErrors) {
            Map<String, Object> map = new HashMap<>();
            map.put("defaultMessage", allError.getDefaultMessage());
            map.put("objectName", allError.getObjectName());
            // 转换类型，获取属性 FieldError extends ObjectError
            // field属性需要从FieldError获取
            FieldError fieldError = (FieldError)allError;
            map.put("field", fieldError.getField());
            list.add(map);
        }
        return AjaxResult.fail("后端数据校验异常", list);
    }
}
