package com.fclever;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

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
}
