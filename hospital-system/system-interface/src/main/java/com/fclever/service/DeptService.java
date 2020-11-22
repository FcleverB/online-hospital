package com.fclever.service;

import com.fclever.domain.Dept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.DeptDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-11-21 20:47
*/
public interface DeptService{

    /**
     * 修改科室信息
     * @param deptDto 待修改的数据
     * @return 是否修改成功的标志
     */
    int updateDept(DeptDto deptDto);

    /**
     * 分页查询科室信息
     * @param deptDto 查询条件
     * @return 查询结果
     */
    DataGridView listForPage(DeptDto deptDto);

    /**
     * 查询所有科室信息（有效）
     * @return 查询结果
     */
    List<Dept> selectAllDept();

    /**
     * 根据id删除科室信息(含批量）
     * @param deptIds 待删除的主键id
     * @return 是否删除成功标志
     */
    int deleteDeptByIds(Long[] deptIds);

    /**
     * 根据id查询指定科室信息
     * @param deptId 指定科室的主键id
     * @return 查询到的科室信息
     */
    Dept getDeptById(Long deptId);

    /**
     * 添加科室信息
     * @param deptDto 待添加的信息（仅保存前端传递的数据，额外的单独设置保存）
     * @return 是否插入成功标志
     */
    int addDept(DeptDto deptDto);
}
