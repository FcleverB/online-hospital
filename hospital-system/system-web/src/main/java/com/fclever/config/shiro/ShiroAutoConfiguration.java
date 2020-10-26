package com.fclever.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Shiro自动配置
 * @author Fclever
 * @create 2020-10-23 13:41
 */
@Configuration
@EnableConfigurationProperties(value = {ShiroProperties.class, RedisProperties.class})
public class ShiroAutoConfiguration {

    // 有了@EnableConfigurationProperties，可以自动注入
    private ShiroProperties shiroProperties;

    // 有了@EnableConfigurationProperties，可以自动注入
    private RedisProperties redisProperties;

    // Shiro过滤器名称
    public static final String SHIRO_FILTER_NAME = "shiroFilter";
    
    
    /**
     * 全参构造器
     * @param shiroProperties
     * @param redisProperties
     */
    public ShiroAutoConfiguration(ShiroProperties shiroProperties, RedisProperties redisProperties) {
        this.shiroProperties = shiroProperties;
        this.redisProperties = redisProperties;
    }

    /**
     * 创建凭证匹配器
     *      将输入的密码进行MD5加密散列两次后与数据库对应密码进行匹配
     *      拿到新密码后与数据库中原有的已经加密过的密码匹配
     */
    @Bean
    public HashedCredentialsMatcher getHashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 注入散列算法名   MD5
        matcher.setHashAlgorithmName(shiroProperties.getHashAlgorithmName());
        // 注入散列次数  2次
        matcher.setHashIterations(shiroProperties.getHashIterations());
        return matcher;
    }

    /**
     * 创建自定义realm，并注入凭证匹配器
     * @param matcher 自定义凭证匹配器
     * @return
     */
    @Bean
    @ConditionalOnClass(value = {UserRealm.class})
    public UserRealm getUserRealm(HashedCredentialsMatcher matcher) {
        UserRealm userRealm = new UserRealm();
        // 注入凭证匹配器
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }

    /**
     * 创建安全管理器
     * @param defaultWebSessionManager  默认的session管理器
     * @param userRealm 自定义Realm
     * @return
     */
    @Bean
    @ConditionalOnClass(value = DefaultWebSecurityManager.class)
    public DefaultWebSecurityManager getSecurityManager(DefaultWebSessionManager defaultWebSessionManager, UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入自定义Realm
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(defaultWebSessionManager);
        return securityManager;
    }

    /**
     * 声明过滤器
     * @param securityManager   安全管理器
     * @return
     */
    @Bean(value = SHIRO_FILTER_NAME)
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        // 创建Shiro的过滤器
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 注入安全管理器
        bean.setSecurityManager(securityManager);
        // 处理用户未认证访问要认证的地址的跳转问题   默认是跳转到shiroProperties.getLoginUrl()现在改成以json串形式返回
        HashMap<String, Filter> filters = new HashMap<>();
        filters.put("authc", new ShiroLoginFilter());
        bean.setFilters(filters);
        HashMap<String, String> map = new HashMap<>();
        // 配置顺序不能错，先配置不拦截，然后配合拦截
        // 配置不拦截的路径
        String[] anonUrls = shiroProperties.getAnonUrls();
        if (anonUrls != null && anonUrls.length > 0){
            for (String anonUrl : anonUrls) {
                map.put(anonUrl, "anon");
            }
        }

        // 配置需要拦截的路径
        String[] authcUrls = this.shiroProperties.getAuthcUrls();
        if (authcUrls != null && authcUrls.length>0){
        for (String authcUrl : authcUrls) {
            map.put(authcUrl, "authc");
        }
    }
    // 配置过滤器
        bean.setFilterChainDefinitionMap(map);
        return bean;
}

    /**
     * 注册DelegatingFilterProxy
     * @return
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> registDelegatingFilterProxy() {
        // 创建注册器
        FilterRegistrationBean<DelegatingFilterProxy> bean = new FilterRegistrationBean<>();
        // 创建过滤器
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        // 注入过滤器
        bean.setFilter(proxy);
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER_NAME);
        Collection<String> servletNames = new ArrayList<>();
        servletNames.add(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        bean.setServletNames(servletNames);
        return bean;
    }

    /**
     * 分布式项目，要使用redis去存我们的登录session
     * @return
     */
    @Bean
    public IRedisManager redisManager() {
        // 因为RedisManager要操作redis，所以必须把redis的客户端给RedisManager
        RedisManager redisManager = new RedisManager();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 获取yml中配置的redis信息
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisProperties.getHost(), redisProperties.getPort(), 10000, redisProperties.getPassword());
        redisManager.setJedisPool(jedisPool);
        return redisManager;
    }

    /**
     * 使用Redis 来存储登录的信息，传递RedisManager给RedisSessionDAO
     * @param redisManager
     * @return  sessionDAO还需要设置给SessionManager
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(IRedisManager redisManager){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager); // 操作哪个redis
        redisSessionDAO.setExpire(7 * 24 * 3600); // 用户的登录信息保存多久？7天
        return redisSessionDAO;
    }

    /**
     * sessionManager里面可以配置sessionDAO
     * @param redisSessionDAO
     * @return
     */
    @Bean
    public DefaultWebSessionManager defaultWebSecurityManager(RedisSessionDAO redisSessionDAO){
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(redisSessionDAO);
        return defaultWebSessionManager;
    }

    /*加入注解的使用，不加入这个注解不生效---开始  如果在Controller中需要用到，那么就要加@Bean注解*/
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /*加入注解的使用，不加入这个注解不生效*/
}
