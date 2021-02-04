package com.fclever.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 保存支付信息的数据传输类
 *      支付订单和支付订单详情
 * @author Fclever
 * @create 2021-02-03 20:40
 */
@ApiModel(value="com-fclever-dto-OrderChargeFormDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderChargeFormDto extends BaseDto {

    // 主订单
    private OrderChargeDto orderChargeDto;

    // 主订单对应的订单详情
    @NotEmpty(message = "订单详情不能为空")
    private List<OrderChargeItemDto> orderChargeItemDtoList;
}
