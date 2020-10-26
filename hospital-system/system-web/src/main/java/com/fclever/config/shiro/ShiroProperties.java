package com.fclever.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Shiro的配置信息
 * @author Fclever
 * @create 2020-10-23 13:12
 */
@ConfigurationProperties(prefix = "shiro") // 一开始会报错，只是声明了但是并没有使用，后面通过@EnableConfigurationProperties使用后就不报错了
@Data
public class ShiroProperties {

    /**
     * 密码加密方式
     */
    private String hashAlgorithmName = "md5";

    /**
     * 密码散列次数
     */
    private Integer hashIterations = 2;

    /**
     * 放行路径
     */
    private String[] anonUrls;

    /**
     * 拦截路径
     */
    private String[] authcUrls;

}
