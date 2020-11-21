package com.fclever.controller.system;

import cn.hutool.core.date.DateUtil;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.constants.HttpStatus;
import com.fclever.domain.LoginLog;
import com.fclever.domain.Menu;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.LoginBodyDto;
import com.fclever.service.LoginLogService;
import com.fclever.service.MenuService;
import com.fclever.utils.AddressUtils;
import com.fclever.utils.IpUtils;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.ActiverUser;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.MenuTreeVo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统关系登录相关
 * @author Fclever
 * @create 2020-10-26 08:52
 */
@RestController
@Log4j2
public class LoginController {

    // 菜单
    @Autowired
    private MenuService menuService;

    // 登录日志
    @Autowired
    private LoginLogService loginLogService;

    /**
     * 登录方法
     *      @Log  默认还有  后台用户+保存请求参数
     *      同时记录登录信息，并封装登录日志LoginLog对象，保存到数据库中
     * @param loginBodyDto 保存用户输入的登录数据
     * @param request
     * @return
     */
    @PostMapping("login/doLogin")
//    @Log(title = "用户登录",businessType = BusinessType.OTHER) 这里就不需要切入日志了，在登录处理逻辑中会将登录的日志记录到登录日志表
    public AjaxResult login(@RequestBody @Validated LoginBodyDto loginBodyDto, HttpServletRequest request){
        // 创建统一返回类型对象
        AjaxResult ajaxResult = AjaxResult.success();
        // 获取输入的登录信息
        String username = loginBodyDto.getUsername();
        String password = loginBodyDto.getPassword();
        // 构造含有用户名和密码的token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        // 封装用户登录信息  保存到登录日志表
        LoginLog loginLog = getLoginInformation(request);
        // 设置登录账号 目前为手机号
        loginLog.setLoginAccount(loginBodyDto.getUsername());
        try{
            subject.login(token);
            // 如果登录成功，将token保存到redis中
            Serializable webToken = subject.getSession().getId();
            ajaxResult.put(Constants.TOKEN, webToken);
            // 登录成功设置状态
            loginLog.setLoginStatus(Constants.LOGIN_SUCCESS);
            // 设置登录用户名
            loginLog.setUserName(ShiroSecurityUtils.getCurrentUserName());
            // 设置成功信息
            loginLog.setMsg("登录成功");
        }catch (Exception e){
            log.error("用户名或密码不正确",e);
            ajaxResult = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
            // 失败之后设置相关信息到登录日志对象
            loginLog.setLoginStatus(Constants.LOGIN_ERROR);
            // 设置登录用户名
            loginLog.setUserName(ShiroSecurityUtils.getCurrentUserName());
            // 设置登录失败信息
            loginLog.setMsg("用户名或密码不正确");
        }
        // 不论成功还是失败都需要将封装好的登录日志信息存入数据库
        this.loginLogService.insertLoginLog(loginLog);
        return ajaxResult;
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("login/getInfo")
    public AjaxResult getInfo() {
        Subject subject = SecurityUtils.getSubject();
        // 获取当前用户的信息|角色|权限
        // 从redis中获取， 在Shiro中做认证的时候，已经将登录用户输入的一些信息保存在了ActiveUser对象中，进而缓存在Redis中
        ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
        AjaxResult ajax = AjaxResult.success();
        // 用户名|头像|角色|权限
        ajax.put("username", activerUser.getUser().getUserName());
        ajax.put("picture", activerUser.getUser().getPicture());
        ajax.put("roles", activerUser.getRoles());
        ajax.put("permissions", activerUser.getPermissions());
        return ajax;
    }

    /**
     * 用户退出
     * @return
     */
    @PostMapping("login/logout")
    @Log(title = "用户退出",businessType = BusinessType.OTHER)
    public AjaxResult logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return AjaxResult.success("用户退出成功");
    }

    /**
     * 获取对应该用户的要显示的菜单
     * @return
     */
    @GetMapping("login/getMenus")
    public AjaxResult getMenus() {
        Subject subject = SecurityUtils.getSubject();
        // 获取当前登录用户信息
        ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
        // 判断类型是否为管理员
        boolean isAdmin = activerUser.getUser().getUserType().equals(Constants.USER_ADMIN);
        // 普通用户
        SimpleUser simpleUser = null;
        // 如果不是超级管理员
        if (!isAdmin){
            simpleUser = new SimpleUser(activerUser.getUser().getUserId(),activerUser.getUser().getUserName());
        }
        // 查询菜单树
        List<Menu> menus = this.menuService.selectMenuTree(isAdmin, simpleUser);
        // 构造返回给前端的菜单值对象
        List<MenuTreeVo> menuVos = new ArrayList<>();
        for (Menu menu : menus) {
            // 菜单id  和菜单路由地址
            menuVos.add(new MenuTreeVo(menu.getMenuId().toString(), menu.getPath()));
        }
        return AjaxResult.success(menuVos);
    }

    /**
     * 继续封装登录日志实体类中的属性
     * @param request   请求
     * @return 登录日志实体类
     */
    private LoginLog getLoginInformation(HttpServletRequest request){
        // 创建登录日志实体对象
        LoginLog loginLog = new LoginLog();
        // 设置IP地址和登录位置
        final String ip = IpUtils.getIpAddr(request);
        loginLog.setIpAddr(ip);
        String address = AddressUtils.getRealAddressByIP(ip);
        loginLog.setLoginLocation(address);
        // User-Agent通常格式：Mozilla/5.0  (平台）  引擎版本  浏览器版本号
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 从User-Agent中获取登录者操作系统
        String os = userAgent.getOperatingSystem().getName();
        loginLog.setOs(os);
        // 从User-Agent中获取登录者浏览器类型
        String browser = userAgent.getBrowser().getName();
        loginLog.setBrowser(browser);
        // 设置登录时间  yyyy-MM-dd HH:mm:ss
        loginLog.setLoginTime(DateUtil.date());
        // 设置登录类型  默认为系统用户
        loginLog.setLoginType(Constants.LOGIN_TYPE_SYSTEM);
        return loginLog;
    }
}
