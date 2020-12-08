package com.fclever;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Fclever
 * @create 2020-10-17 07:35
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.fclever.mapper"})
@EnableDubbo
public class ErpApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
        System.out.println("系统模块hospital-erp启动成功");
    }
}
