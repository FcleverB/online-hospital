package com.fclever.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 远程调用其他服务时，需要传递当前登录用户一些必要信息时，可以传递该对象
 *
 * @author Fclever
 * @create 2020-10-19 13:50
 */
@Data // 自动生成setter|getter等方法
@AllArgsConstructor // 生成全部参数的构造方法
@NoArgsConstructor // 生成无参构造
public class SimpleUser implements Serializable {
    private Serializable userId;
    private String userName;
}
