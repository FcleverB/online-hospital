package com.fclever.config.shiro;

import com.fclever.domain.User;
import com.fclever.service.UserService;
import com.fclever.vo.ActiverUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * 认证和授权
 *          自定义Realm去匹配用户名和密码
 * @author Fclever
 * @create 2020-10-22 08:21
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy // 懒加载，使用到该实例的时候再去初始化
    private UserService userService;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 做认证---登录操作
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户登录名   getPrincipal输入的用户名    getCredentials输入的密码
        String phone = token.getPrincipal().toString();
        // 根据电话查询用户是否存在
        User user = this.userService.queryUserByPhone(phone);
        if (null != user){ // 用户名验证正确，继续验证密码
            // 组装存放到redis中的对象ActiverUser
            /**
             * 从上面流程来看，对登录名做了验证，但是并没有获取输入的密码，然后对密码进行校验
             *      上面只是通过输入用户名从数据库中查询到了该条用户信息（包括密码，盐值）
             *      继承关系：shiroRealm----->AuthorizingRealm---->AuthenticatingRealm
             *      构建SimpleAuthenticationInfo的时候
             *      当执行"return info"之后，会调用AuthenticatingRealm的getAuthenticationInfo()方法
             *      在该方法中，调用了assertCredentialsMatch(token, info);这里面就会对token中保存的输入密码进行盐值加密，然后与数据库中密码进行匹配
             */
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                    activerUser, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName()
            );
            return info;
        }
        // 用户不存在返回null
        return null;
    }

    /**
     * 做授权---判断登录用户是否拥有对菜单或者按钮的操作权限
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 该方法得到的对象就是doGetAuthenticationInfo方法返回的SimpleAuthenticationInfo中构造方法中Object principal
        ActiverUser activerUser = (ActiverUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }
}
