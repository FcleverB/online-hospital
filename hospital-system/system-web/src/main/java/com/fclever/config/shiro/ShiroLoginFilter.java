package com.fclever.config.shiro;

import com.alibaba.fastjson.JSON;
import com.fclever.constants.HttpStatus;
import com.fclever.vo.AjaxResult;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录过滤器
 *      判断用户是否登录
 * @author Fclever
 * @create 2020-10-21 13:50
 */
public class ShiroLoginFilter extends FormAuthenticationFilter {

    /**
     * 在访问controller前进行判断是否登录，返回json，不进行重定向
     *          后端不关心页面的跳转，只负责数据的交互
     * @param request
     * @param response
     * @return true--继续往下执行，false--
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 获取HttpServletRequest对象
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 设置响应字符编码UTF-8
        httpServletResponse.setCharacterEncoding("UTF-8");
        // 响应数据类型json
        httpServletResponse.setContentType("application/json");
        // 规范统一响应数据内容
        AjaxResult ajaxResult = AjaxResult.fail();
        // 状态码：未授权
        ajaxResult.put("code", HttpStatus.UNAUTHORIZED);
        // 返回消息
        ajaxResult.put("msg", "登录认证失效，请重新登录！");
        // 输出
        httpServletResponse.getWriter().write(JSON.toJSON(ajaxResult).toString());
        return false;
    }
}
