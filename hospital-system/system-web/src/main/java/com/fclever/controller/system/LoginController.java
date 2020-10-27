package com.fclever.controller.system;

import com.fclever.constants.Constants;
import com.fclever.constants.HttpStatus;
import com.fclever.domain.Menu;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.LoginBodyDto;
import com.fclever.service.MenuService;
import com.fclever.vo.ActiverUser;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.MenuTreeVo;
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

    @Autowired
    private MenuService menuService;

    /**
     * 登录方法
     * @param loginBodyDto 保存用户输入的登录数据
     * @param request
     * @return
     */
    @PostMapping("login/doLogin")
    public AjaxResult login(@RequestBody @Validated LoginBodyDto loginBodyDto, HttpServletRequest request){
        // 创建统一返回类型对象
        AjaxResult ajaxResult = AjaxResult.success();
        // 获取输入的登录信息
        String username = loginBodyDto.getUsername();
        String password = loginBodyDto.getPassword();
        // 构造含有用户名和密码的token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);
            // 如果登录成功，将token保存到redis中
            Serializable webToken = subject.getSession().getId();
            ajaxResult.put(Constants.TOKEN, webToken);
        }catch (Exception e){
            log.error("用户名或密码不正确",e);
            ajaxResult = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
        }
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
        List<Menu> menus = menuService.selectMenuTree(isAdmin, simpleUser);
        // 构造返回给前端的菜单值对象
        List<MenuTreeVo> menuVos = new ArrayList<>();
        for (Menu menu : menus) {
            // 菜单id  和菜单路由地址
            menuVos.add(new MenuTreeVo(menu.getMenuId().toString(), menu.getPath()));
        }
        return AjaxResult.success(menuVos);
    }
}
