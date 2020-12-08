package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.dto.ProducterDto;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;
import com.fclever.domain.Producter;
import com.fclever.mapper.ProducterMapper;
/**
@author Fclever
@create 2020-12-07 13:28
*/
@Service
public class ProducterServiceImpl implements ProducterService{

    @Autowired
    private ProducterMapper producterMapper;

    /**
     * 修改生产厂家信息
     * @param producterDto 修改的生产厂家信息
     * @return 是否修改成功标志
     */
    @Override
    public int updateProducter(ProducterDto producterDto) {
        // 创建实体对象
        Producter producter = new Producter();
        // 值拷贝
        BeanUtil.copyProperties(producterDto, producter);
        // 设置修改人
        producter.setUpdateBy(producterDto.getSimpleUser().getUserName());
        // 执行更新操作
        return this.producterMapper.updateById(producter);
    }

    /**
     * 分页查询生产厂家信息
     * @param producterDto 待修改的生产厂家信息
     * @return 分页数据
     */
    @Override
    public DataGridView listProducterForPage(ProducterDto producterDto) {
        // 创建分页对象
        Page<Producter> page = new Page<>(producterDto.getPageNum(), producterDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<Producter> qw = new QueryWrapper<>();
        // 封装查询条件
        // 厂家名称模糊匹配
        qw.like(StringUtils.isNotBlank(producterDto.getProducterName()), Producter.COL_PRODUCTER_NAME, producterDto.getProducterName());
        // 关键字模糊匹配
        qw.like(StringUtils.isNotBlank(producterDto.getKeywords()), Producter.COL_KEYWORDS, producterDto.getKeywords());
        // 电话模糊匹配
        qw.like(StringUtils.isNotBlank(producterDto.getProducterTel()), Producter.COL_PRODUCTER_TEL, producterDto.getProducterTel());
        // 状态精确匹配
        qw.eq(StringUtils.isNotBlank(producterDto.getStatus()), Producter.COL_STATUS, producterDto.getStatus());
        // 创建时间范围匹配
        qw.ge(producterDto.getBeginTime() != null, Producter.COL_CREATE_TIME,producterDto.getBeginTime());
        qw.le(producterDto.getEndTime() != null, Producter.COL_CREATE_TIME,producterDto.getEndTime());
        // 根据创建时间升序排序
        qw.orderByAsc(Producter.COL_CREATE_TIME);
        // 执行查询
        this.producterMapper.selectPage(page, qw);
        // 封装分页对象并返回结果
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id删除生产厂家（含批量）
     * @param producterIds 待删除的生产厂家id集合
     * @return 是否删除成功标志
     */
    @Override
    public int deleteProducterByIds(Long[] producterIds) {
        List<Long> producterIdsList = Arrays.asList(producterIds);
        if (producterIdsList != null && producterIdsList.size() > 0){
            return this.producterMapper.deleteBatchIds(producterIdsList);
        }
        return 0;
    }

    /**
     * 根据id查询对应的生产厂家信息
     * @param producterId 待查询的生产厂家id
     * @return 查询结果
     */
    @Override
    public Producter getProducterById(Long producterId) {
        return this.producterMapper.selectById(producterId);
    }

    /**
     * 查询所有可用的生产厂家信息
     * @return 查询结果
     */
    @Override
    public List<Producter> selectAllProducter() {
        // 创建查询条件对象
        QueryWrapper<Producter> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(Producter.COL_STATUS, Constants.STATUS_TRUE);
        // 执行查询
        return this.producterMapper.selectList(qw);
    }

    /**
     * 添加生产厂家信息
     * @param producterDto 带添加的数据
     * @return 是否添加成功标志
     */
    @Override
    public int addProducter(ProducterDto producterDto) {
        // 创建实体对象
        Producter producter = new Producter();
        // 值拷贝
        BeanUtil.copyProperties(producterDto, producter);
        // 设置创建人和创建时间
        producter.setCreateBy(producterDto.getSimpleUser().getUserName());
        producter.setCreateTime(DateUtil.date());
        // 执行插入操作
        return this.producterMapper.insert(producter);
    }
}
