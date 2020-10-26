package com.fclever.config.shiro;

import com.fclever.constants.Constants;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.UUID;

/**
 * Token生成管理
 *      如果有token，从请求头中取出后直接返回前台
 *      如果没有就生成一个
 * @author Fclever
 * @create 2020-10-22 07:58
 */
@Configuration
public class TokenWebSessionManager extends DefaultWebSessionManager {

    /**
     * 获取请求头中保存的token，如果存在则返回，不存在则生成
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //从请求头里面得到请求携带的TOKEN 如果不存在就生成一个
        String token= WebUtils.toHttp(request).getHeader(Constants.TOKEN);
        // 如果token为null、“”、“ ” 返回false，否则返回true
        if(StringUtils.hasText(token)){
            return token;
        }else{
            // 使用UIID生成一个数
            return UUID.randomUUID().toString();
        }
    }
}