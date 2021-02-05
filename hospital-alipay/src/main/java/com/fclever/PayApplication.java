package com.fclever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Fclever
 * @create 2021-02-05 12:40
 */
@SpringBootApplication
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
        System.out.println("支付模块启动成功");
    }
}
