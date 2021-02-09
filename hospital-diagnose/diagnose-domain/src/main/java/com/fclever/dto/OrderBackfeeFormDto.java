package com.fclever.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 需要保存的退费主表和详情信息
 * @author Fclever
 * @create 2021-02-09 09:57
 */
@ApiModel(value="com-fclever-dto-OrderBackfeeFormDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderBackfeeFormDto extends BaseDto{

    // 退费主表数据
    private OrderBackfeeDto orderBackfeeDto;

    // 退费详情信息
    @NotEmpty(message = "退费详情列表不能为空")
    private List<OrderBackfeeItemDto> orderBackfeeItemDtoList;
}
