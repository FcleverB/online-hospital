package com.fclever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fclever.domain.Medicines;
import org.apache.ibatis.annotations.Param;

/**
 * @author Fclever
 * @create 2020-12-12 22:11
 */
public interface MedicinesMapper extends BaseMapper<Medicines> {

    /**
     * 扣减库存
     *
     * @param medicinesId 药品Id
     * @param num         需要扣减的数量
     * @return 操作结果
     */
    int deductMedicines(@Param("medicinesId") Long medicinesId, @Param("num") long num);
}