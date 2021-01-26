package com.fclever.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 页面需要保存的处方和处方详情信息
 * @author Fclever
 * @create 2021-01-26 13:59
 */
@ApiModel(value="com-fclever-dto-CareOrderFormDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareOrderFormDto implements Serializable {

    // 处方
    private CareOrderDto careOrder;

    // 处方详情
    @NotEmpty(message = "处方详情不能为空")
    private List<CareOrderItemDto> careOrderItems;
}
