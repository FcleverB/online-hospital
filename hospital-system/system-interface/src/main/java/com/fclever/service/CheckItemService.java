package com.fclever.service;

import com.fclever.domain.CheckItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.CheckItemDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-12-05 12:36
*/
public interface CheckItemService{

    /**
     * 修改检查项目
     * @param checkItemDto 待修改内容
     * @return 是否修改成功标志
     */
    int updateCheckItem(CheckItemDto checkItemDto);

    /**
     * 分页查询检查项目
     * @param checkItemDto 查询条件
     * @return 查询结果
     */
    DataGridView listCheckItemForPage(CheckItemDto checkItemDto);

    /**
     * 根据id删除检查项目（含批量）
     * @param checkItemIds  待删除的检查项目id
     * @return 是否删除成功标志
     */
    int deleteCheckItemByIds(Long[] checkItemIds);

    /**
     * 根据id查询检查项目
     * @param checkItemId 待查询的检查项目id
     * @return 查询到的结果
     */
    CheckItem getCheckItemById(Long checkItemId);

    /**
     * 查询所有可用的检查项目
     * @return 查询结果
     */
    List<CheckItem> selectAllCheckItem();

    /**
     * 添加检查项目
     * @param checkItemDto  待添加的内容
     * @return 是否添加成功标志
     */
    int addCheckItem(CheckItemDto checkItemDto);
}
