package com.fclever.domain;

import java.io.Serializable;

/**
 * 基础父类
 *      后续各个模块中生成的实体类都需要继承该类
 *      因为做远程调用的类必须要进行序列化，这里统一做序列化处理
 *      然后新的实体类只需要统一继承该类即可
 * @author Fclever
 * @create 2020-10-19 13:48
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID=1L;
}
