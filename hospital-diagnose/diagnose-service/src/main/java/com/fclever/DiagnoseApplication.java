package com.fclever;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Fclever
 * @create 2020-10-17 09:18
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.fclever.mapper"})
@EnableDubbo
public class DiagnoseApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiagnoseApplication.class, args);
    }
}
