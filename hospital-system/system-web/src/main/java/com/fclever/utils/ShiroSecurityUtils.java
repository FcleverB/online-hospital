package com.fclever.utils;

import com.fclever.constants.Constants;
import com.fclever.domain.SimpleUser;
import com.fclever.domain.User;
import com.fclever.vo.ActiverUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * 获取当前登录用户相关信息
 * @author Fclever
 * @create 2020-11-03 12:30
 */
public class ShiroSecurityUtils {

    /**
     * 得到当前登陆的用户对象的ActiveUser（包括用户基本信息+角色+权限）
     *      存储在Redis中
     * @return
     */
    public static ActiverUser getCurrentActiveUser(){
        Subject subject= SecurityUtils.getSubject();
        ActiverUser activerUser= (ActiverUser) subject.getPrincipal();
        return activerUser;
    }

    /**
     * 得到当前登陆的用户对象User
     * @return
     */
    public static User getCurrentUser(){
        return getCurrentActiveUser().getUser();
    }

    /**
     * 得到当前登陆的用户对象SimpleUser（用户id+用户姓名）
     * @return
     */
    public static SimpleUser getCurrentSimpleUser(){
        User user = getCurrentActiveUser().getUser();
        return new SimpleUser(user.getUserId(),user.getUserName());
    }

    /**
     * 得到当前登陆的用户名称
     * @return
     */
    public static String getCurrentUserName(){
        return getCurrentActiveUser().getUser().getUserName();
    }

    /**
     * 得到当前登陆对象的角色编码
     * @return
     */
    public static List<String> getCurrentUserRoles(){
        return getCurrentActiveUser().getRoles();
    }


    /**
     * 得到当前登陆对象的权限编码
     * @return
     */
    public static List<String> getCurrentUserPermissions(){
        return getCurrentActiveUser().getPermissions();
    }

    /**
     * 判断当前用户是否是超级管理员
     * @return
     */
    public static boolean isAdmin(){
        return getCurrentUser().getUserType().equals(Constants.USER_ADMIN);
    }
}
