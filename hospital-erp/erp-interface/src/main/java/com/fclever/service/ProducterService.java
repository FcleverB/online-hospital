package com.fclever.service;

import com.fclever.domain.Producter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.ProducterDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-12-07 13:28
*/
public interface ProducterService{

    /**
     * 修改生产厂家信息
     * @param producterDto 修改的生产厂家信息
     * @return 是否修改成功标志
     */
    int updateProducter(ProducterDto producterDto);

    /**
     * 分页查询生产厂家信息
     * @param producterDto 待修改的生产厂家信息
     * @return 分页数据
     */
    DataGridView listProducterForPage(ProducterDto producterDto);

    /**
     * 根据id删除生产厂家（含批量）
     * @param producterIds 待删除的生产厂家id集合
     * @return 是否删除成功标志
     */
    int deleteProducterByIds(Long[] producterIds);

    /**
     * 根据id查询对应的生产厂家信息
     * @param producterId 待查询的生产厂家id
     * @return 查询结果
     */
    Producter getProducterById(Long producterId);

    /**
     * 查询所有可用的生产厂家信息
     * @return 查询结果
     */
    List<Producter> selectAllProducter();

    /**
     * 添加生产厂家信息
     * @param producterDto 带添加的数据
     * @return 是否添加成功标志
     */
    int addProducter(ProducterDto producterDto);
}
