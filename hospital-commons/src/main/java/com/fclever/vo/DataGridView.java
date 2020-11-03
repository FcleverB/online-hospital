package com.fclever.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询封装类
 *      总条数+实际数据
 * @author Fclever
 * @create 2020-11-02 20:32
 */
@ApiModel(value="com-fclever-vo-DataGridView")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridView implements Serializable {

    // 查询总条数
    private Long total;
    // 查询到的数据
    private List<?> data;
}
