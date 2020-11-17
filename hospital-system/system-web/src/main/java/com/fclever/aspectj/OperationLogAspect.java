package com.fclever.aspectj;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.OperatorStatus;
import com.fclever.domain.OperationLog;
import com.fclever.domain.User;
import com.fclever.service.OperationLogService;
import com.fclever.utils.AddressUtils;
import com.fclever.utils.IpUtils;
import com.fclever.utils.ServletUtils;
import com.fclever.utils.ShiroSecurityUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * 系统操作日志切面类
 *      @Component:注册为组件交给SpringIOC管理
 *      @Aspect 声明为切面
 *      @Log4j2 日志记录
 * @author Fclever
 * @create 2020-11-16 14:33
 */
@Component
@Aspect
@Log4j2
public class OperationLogAspect {

    // 保存日志的接口
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 声明切面的切入点----一方法的形式，后面使用该切点的时候直接使用方法名即可
     *      指定只要Controller中使用了@Log注解的方法都需要使用切入
     */
    @Pointcut("@annotation(com.fclever.aspectj.annotation.Log)")
    public void logPointCut(){}

    /**
     * 后置通知
     *      正常处理完请求后执行
     *      pointcut：指定匹配的切点
     *      returning：获取m目标方法的返回值
     * @param joinPoint 匹配的切点
     * @param jsonResult    目标方法的返回值（该项目为json类型）
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        // 调用处理日志的方法
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 异常通知
     *      目标方法出现异常时执行
     * @param joinPoint 切点
     * @param e 目标方法抛出的异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        // 调用处理日志的方法
        handleLog(joinPoint, e, null);
    }

    /**
     * 处理日志，并保存到数据库
     * @param joinPoint 匹配的切点
     * @param e 目标方法抛出的异常
     * @param jsonResult    目标方法的返回值（该项目为json类型）
     */
    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 根据切入点表达式获取注解
            Log annotationLog = getAnnotationLog(joinPoint);
            if (annotationLog == null) {
                return;
            }

            // 获取当前的用户
            User loginUser = ShiroSecurityUtils.getCurrentUser();

            /*---------------操作日志实体对象------------*/
            // 创建操作日志对象
            OperationLog operationLog = new OperationLog();
            // 1. 设置状态   ordinal就是返回枚举对象在枚举类型中的序数 从0开始
            operationLog.setStatus(String.valueOf(OperatorStatus.SUCCESS.ordinal()));
            // 2. 设置本次操作的IP地址  1.202.74.201
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            operationLog.setOperIp(ip);
            // 3. 设置本次操作的真实地址  北京市北京 电信
            String address = AddressUtils.getRealAddressByIP(ip);
            operationLog.setOperLocation(address);
            // 4. 设置请求的返回参数到操作日志对象中
            operationLog.setJsonResult(JSON.toJSONString(jsonResult));
            // 5. 设置请求URL
            operationLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            // 6. 将登录用户设置为操作人员名称
            if (loginUser != null){
                operationLog.setOperName(loginUser.getUserName());
            }
            // 7. 如果目标方法抛出了异常，设置状态为FAIL，并且设置错误信息
            if (e != null) {
                operationLog.setStatus(String.valueOf(OperatorStatus.FAIL.ordinal()));
                // 数据库保存错误信息的字典长度就是2000
                operationLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 8. 设置操作的方法名称 类名称+类中的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operationLog.setMethod(className + "." + methodName + "()");
            // 9. 设置请求方式
            operationLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 10. 处理@Log注解上的参数
            getControllerMethodDescription(joinPoint, annotationLog, operationLog);
            // 11. 设置操作执行时间为当前时间
            operationLog.setOperTime(DateUtil.date());
            // 保存数据库
            operationLogService.insertOperationLog(operationLog);
        } catch (Exception exception) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exception.getMessage());
            exception.printStackTrace();
        }
    }


    /**
     * 根据切入点表达式获取对应的注解对象
     * @param joinPoint 切入点表达式
     * @return 切入点表达式对应的注解对象
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 获取使用了@Log注解的方法的描述信息 用于Controller层注解
     * @param joinPoint 切入点表达式
     * @param controllerLog     @Log注解的实体对象
     * @param operationLog  操作日志实体对象
     */
    private void getControllerMethodDescription(JoinPoint joinPoint, Log controllerLog, OperationLog operationLog) {
        // 设置业务类型   从Controller层方法获取时增删改还是其他，这些信息在Controller中的@Log注解中可以获取到
        operationLog.setBusinessType(String.valueOf(controllerLog.businessType().ordinal()));
        // 设置模块标题
        operationLog.setTitle(controllerLog.title());
        // 设置操作人员类别   后台用户  手机端用户  其他，这些信息在控制层方法上的@Log注解上可以获取
        operationLog.setOperatorType(controllerLog.operatorType().ordinal());
        // 根据Controller层中方法的@Log注解的是否需要保存请求参数的内容，来决定是否保存请求参数到操作日志对象中
        if (controllerLog.isSaveRequestData()) {
            // 获取参数的信息，赋值到操作日志对象中
            setRequestValue(joinPoint, operationLog);
        }
    }

    /**
     * 获取请求的参数，放到操作日志对象中
     * @param joinPoint 切入点
     * @param operationLog  操作日志对象
     */
    private void setRequestValue(JoinPoint joinPoint, OperationLog operationLog) {
        // 获取请求方式post，get，put，delete等
        String requestMethod = operationLog.getRequestMethod();
        // put和post请求
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            // 设置请求参数
            operationLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            // 设置请求参数
            operationLog.setOperParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 参数拼装
     * @param paramsArray
     * @return
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }
    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o)
    {
        return o instanceof MultipartFile || o instanceof HttpServletRequest
                || o instanceof HttpServletResponse;
    }
}
