package com.fclever;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;

/**
 * @author Fclever
 * @create 2020-10-16 08:43
 */
@SpringBootApplication // SpringBoot项目核心注解，开启自动配置
@MapperScan(basePackages = {"com.fclever.mapper"})  // 指定接口所在包，对应包下的接口编译后会生成相应实现类
@EnableDubbo // 启动Dubbo
@EnableHystrix // 启用Hystrix
@EnableCircuitBreaker // 启动Hystrix 断路保护
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        System.out.println("系统模块hospital-system启动成功");
    }

    // 解决高版本Tomcat助攻出现RFC 7230和RFC 3986错误，即解决对接口参数字符的限制
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]"));
        return factory;
    }

}
