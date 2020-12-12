package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.domain.Medicines;
import com.fclever.dto.MedicinesDto;
import com.fclever.mapper.MedicinesMapper;
import com.fclever.service.MedicinesService;
import com.fclever.vo.DataGridView;

import java.util.Arrays;
import java.util.List;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
@author Fclever
@create 2020-12-12 22:11
*/
// 指定该类中的addMedicines方法的请求重试次数为0
@Service(methods = {@Method(name = "addMedicines", retries = 0)})
public class MedicinesServiceImpl implements MedicinesService {

    @Autowired
    private MedicinesMapper medicinesMapper;

    /**
     * 修改药品信息信息
     * @param medicinesDto 修改的药品信息信息
     * @return 是否修改成功标志
     */
    @Override
    public int updateMedicines(MedicinesDto medicinesDto) {
        // 创建实体对象
        Medicines medicines = new Medicines();
        // 值拷贝
        BeanUtil.copyProperties(medicinesDto, medicines);
        // 设置修改人
        medicines.setUpdateBy(medicinesDto.getSimpleUser().getUserName());
        // 执行更新操作
        return this.medicinesMapper.updateById(medicines);
    }

    /**
     * 分页查询药品信息信息
     * @param medicinesDto 待修改的药品信息信息
     * @return 分页数据
     */
    @Override
    public DataGridView listMedicinesForPage(MedicinesDto medicinesDto) {
        // 创建分页对象
        Page<Medicines> page = new Page<>(medicinesDto.getPageNum(), medicinesDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<Medicines> qw = new QueryWrapper<>();
        // 封装查询条件
        // 药品名称模糊匹配
        qw.like(StringUtils.isNotBlank(medicinesDto.getMedicinesName()), Medicines.COL_MEDICINES_NAME, medicinesDto.getMedicinesName());
        // 关键字模糊匹配
        qw.like(StringUtils.isNotBlank(medicinesDto.getKeywords()), Medicines.COL_KEYWORDS, medicinesDto.getKeywords());
        // 药品类型精确匹配
        qw.like(StringUtils.isNotBlank(medicinesDto.getMedicinesType()), Medicines.COL_MEDICINES_TYPE, medicinesDto.getMedicinesType());
        // 生产厂家精确匹配
        qw.like(StringUtils.isNotBlank(medicinesDto.getProducterId()), Medicines.COL_PRODUCTER_ID, medicinesDto.getProducterId());
        // 处方类型精确匹配
        qw.like(StringUtils.isNotBlank(medicinesDto.getPrescriptionType()), Medicines.COL_PRESCRIPTION_TYPE, medicinesDto.getPrescriptionType());
        // 状态精确匹配
        qw.eq(StringUtils.isNotBlank(medicinesDto.getStatus()), Medicines.COL_STATUS, medicinesDto.getStatus());
        // 根据创建时间升序排序
        qw.orderByAsc(Medicines.COL_CREATE_TIME);
        // 执行查询
        this.medicinesMapper.selectPage(page, qw);
        // 封装分页对象并返回结果
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id删除药品信息（含批量）
     * @param medicinesIds 待删除的药品信息id集合
     * @return 是否删除成功标志
     */
    @Override
    public int deleteMedicinesByIds(Long[] medicinesIds) {
        List<Long> medicinesIdsList = Arrays.asList(medicinesIds);
        if (medicinesIdsList != null && medicinesIdsList.size() > 0){
            return this.medicinesMapper.deleteBatchIds(medicinesIdsList);
        }
        return 0;
    }

    /**
     * 根据id查询对应的药品信息信息
     * @param medicinesId 待查询的药品信息id
     * @return 查询结果
     */
    @Override
    public Medicines getMedicinesById(Long medicinesId) {
        return this.medicinesMapper.selectById(medicinesId);
    }

    /**
     * 查询所有可用的药品信息信息
     * @return 查询结果
     */
    @Override
    public List<Medicines> selectAllMedicines() {
        // 创建查询条件对象
        QueryWrapper<Medicines> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(Medicines.COL_STATUS, Constants.STATUS_TRUE);
        // 执行查询
        return this.medicinesMapper.selectList(qw);
    }

    /**
     * 添加药品信息信息
     * @param medicinesDto 带添加的数据
     * @return 是否添加成功标志
     */
    @Override
    public int addMedicines(MedicinesDto medicinesDto) {
        // 创建实体对象
        Medicines medicines = new Medicines();
        // 值拷贝
        BeanUtil.copyProperties(medicinesDto, medicines);
        // 设置创建人和创建时间
        medicines.setCreateBy(medicinesDto.getSimpleUser().getUserName());
        medicines.setCreateTime(DateUtil.date());
        // 执行插入操作
        return this.medicinesMapper.insert(medicines);
    }

    /**
     * 修改指定药品的库存量
     * @param medicinesId 指定的药品主键id
     * @param medicinesStockNum 要调整的库存量
     * @return 是否修改成功标志
     */
    @Override
    public int updateMedicinesStock(Long medicinesId, Long medicinesStockNum) {
        // 创建实体对象
        Medicines medicines = new Medicines();
        // 设置值
        medicines.setMedicinesId(medicinesId);
        medicines.setMedicinesStockNum(medicinesStockNum);
        // 设置修改人----未实现
        // 执行更新操作
        return this.medicinesMapper.updateById(medicines);
    }
}
