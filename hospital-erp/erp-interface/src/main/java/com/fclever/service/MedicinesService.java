package com.fclever.service;

import com.fclever.domain.Medicines;
import com.fclever.dto.MedicinesDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-12-12 22:11
*/
public interface MedicinesService{

    /**
     * 修改药品信息信息
     * @param medicinesDto 修改的药品信息信息
     * @return 是否修改成功标志
     */
    int updateMedicines(MedicinesDto medicinesDto);

    /**
     * 分页查询药品信息信息
     * @param medicinesDto 待修改的药品信息信息
     * @return 分页数据
     */
    DataGridView listMedicinesForPage(MedicinesDto medicinesDto);

    /**
     * 根据id删除药品信息（含批量）
     * @param medicinesIds 待删除的药品信息id集合
     * @return 是否删除成功标志
     */
    int deleteMedicinesByIds(Long[] medicinesIds);

    /**
     * 根据id查询对应的药品信息信息
     * @param medicinesId 待查询的药品信息id
     * @return 查询结果
     */
    Medicines getMedicinesById(Long medicinesId);

    /**
     * 查询所有可用的药品信息信息
     * @return 查询结果
     */
    List<Medicines> selectAllMedicines();

    /**
     * 添加药品信息信息
     * @param medicinesDto 带添加的数据
     * @return 是否添加成功标志
     */
    int addMedicines(MedicinesDto medicinesDto);

    /**
     * 修改指定药品的库存量
     * @param medicinesId 指定的药品主键id
     * @param medicinesStockNum 要调整的库存量
     * @return 是否修改成功标志
     */
    int updateMedicinesStock(Long medicinesId, Long medicinesStockNum);
}
