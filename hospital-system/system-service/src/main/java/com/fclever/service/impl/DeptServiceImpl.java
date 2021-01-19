package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.dto.DeptDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import com.fclever.mapper.DeptMapper;
import com.fclever.domain.Dept;
import com.fclever.service.DeptService;
/**
@author Fclever
@create 2020-11-21 20:47
*/
@Service
public class DeptServiceImpl implements DeptService{

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 修改科室信息
     * @param deptDto 待修改的数据
     * @return 是否修改成功的标志
     */
    @Override
    public int updateDept(DeptDto deptDto) {
        // 形参为前后端数据传输的内容，创建与数据库交互的完全实体对象
        Dept dept = new Dept();
        // 拷贝
        BeanUtil.copyProperties(deptDto, dept);
        dept.setUpdateBy(deptDto.getSimpleUser().getUserName());
        // 更新时间字段在数据库进行了设置
        return this.deptMapper.updateById(dept);
    }

    /**
     * 分页查询科室信息
     * @param deptDto 查询条件
     * @return 查询结果
     */
    @Override
    public DataGridView listForPage(DeptDto deptDto) {
        // 创建分页对象
        Page<Dept> page = new Page<>(deptDto.getPageNum(), deptDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<Dept> qw = new QueryWrapper<>();
        // 模糊匹配  科室名称
        qw.like(StringUtils.isNotBlank(deptDto.getDeptName()), Dept.COL_DEPT_NAME, deptDto.getDeptName());
        // 精确匹配状态
        qw.eq(StringUtils.isNotBlank(deptDto.getStatus()), Dept.COL_STATUS, deptDto.getStatus());
        // 创建时间范围匹配
        qw.ge(deptDto.getBeginTime() != null, Dept.COL_CREATE_TIME, deptDto.getBeginTime());
        qw.le(deptDto.getEndTime() != null, Dept.COL_CREATE_TIME, deptDto.getEndTime());
        // 根据显示顺序进行排序
        qw.orderByAsc(Dept.COL_ORDER_NUM);
        // 执行查询
        this.deptMapper.selectPage(page, qw);
        // 封装结果并返回
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 查询所有科室信息（有效）
     * @return 查询结果
     */
    @Override
    public List<Dept> selectAllDept() {
        // 创建查询条件对象
        QueryWrapper<Dept> qw = new QueryWrapper<>();
        // 精确匹配状态
        qw.eq(Dept.COL_STATUS, Constants.STATUS_TRUE);
        // 根据显示顺序进行排序
        qw.orderByAsc(Dept.COL_ORDER_NUM);
        // 封装结果并返回
        return this.deptMapper.selectList(qw);
    }

    /**
     * 根据id删除科室信息(含批量）
     * @param deptIds 待删除的主键id
     * @return 是否删除成功标志
     */
    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        // 转成数组
        List<Long> deptList = Arrays.asList(deptIds);
        if (deptList != null && deptList.size() > 0){
            return this.deptMapper.deleteBatchIds(deptList);
        }
        return 0;
    }

    /**
     * 根据id查询指定科室信息
     * @param deptId 指定科室的主键id
     * @return 查询到的科室信息
     */
    @Override
    public Dept getDeptById(Long deptId) {
        return this.deptMapper.selectById(deptId);
    }

    /**
     * 添加科室信息
     * @param deptDto 待添加的信息（仅保存前端传递的数据，额外的单独设置保存）
     * @return 是否插入成功标志
     */
    @Override
    public int addDept(DeptDto deptDto) {
        // 创建保存到数据库的实体对象，涉及到与数据库的增加要用完全实体类
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptDto, dept);
        dept.setCreateBy(deptDto.getSimpleUser().getUserName());
        dept.setCreateTime(DateUtil.date());
        return this.deptMapper.insert(dept);
    }

    /**
     * 根据科室id集合，查询对应的科室信息
     * @param deptIds 需要查询的科室id集合
     * @return  查询结果
     */
    @Override
    public List<Dept> listDeptByDeptIds(List<Long> deptIds) {
        // 创建查询条件对象
        QueryWrapper<Dept> qw = new QueryWrapper<>();
        qw.in(Dept.COL_DEPT_ID, deptIds);
        return this.deptMapper.selectList(qw);
    }

    /**
     * 根据科室id更新挂号数量
     * @param deptId    科室id
     * @param regNumber 要更新的挂号数量
     */
    @Override
    public void updateRegNumberByDeptId(Long deptId, int regNumber) {
        Dept dept = new Dept();
        dept.setDeptId(deptId);
        dept.setRegNumber(regNumber);
        this.deptMapper.updateById(dept);
    }
}
