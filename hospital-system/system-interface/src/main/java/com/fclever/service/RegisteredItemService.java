package com.fclever.service;

import com.fclever.domain.RegisteredItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.RegisteredItemDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
 @author Fclever
 @create 2020-12-05 16:18
 */
public interface RegisteredItemService{

    /**
     * 修改挂号信息
     * @param registeredItemDto 待修改数据
     * @return 是否修改成功标志
     */
    int updateRegisteredItem(RegisteredItemDto registeredItemDto);

    /**
     * 分页查询所有挂号信息
     * @param registeredItemDto 查询条件信息
     * @return 查询结果
     */
    DataGridView listRegisteredItemForPage(RegisteredItemDto registeredItemDto);

    /**
     * 查询所有可用的挂号信息
     * @return 查询结果
     */
    List<RegisteredItem> selectAllRegisteredItem();

    /**
     * 根据id删除对应挂号信息（含批量）
     * @param regItemId 待删除的挂号id
     * @return 是否删除成功的标志
     */
    int deleteRegiteredItemByIds(Long[] regItemId);

    /**
     * 根据id查询对应的挂号信息
     * @param regItemId 要查询的挂号id
     * @return 查询结果
     */
    RegisteredItem getRegisteredItemById(Long regItemId);

    /**
     * 添加挂号费用信息
     * @param registeredItemDto 待添加的信息
     * @return 是否添加成功的标志
     */
    int addRegisteredItem(RegisteredItemDto registeredItemDto);
}
