package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.domain.CheckItem;
import com.fclever.dto.CheckItemDto;
import com.fclever.mapper.CheckItemMapper;
import com.fclever.service.CheckItemService;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
/**
@author Fclever
@create 2020-12-05 12:36
*/
@Service
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemMapper checkItemMapper;

    /**
     * 修改检查项目
     * @param checkItemDto 待修改内容
     * @return 是否修改成功标志
     */
    @Override
    public int updateCheckItem(CheckItemDto checkItemDto) {
        // 创建检查项目实体
        CheckItem checkItem = new CheckItem();
        // 值拷贝
        BeanUtil.copyProperties(checkItemDto, checkItem);
        // 设置修改人和修改时间
        checkItem.setUpdateBy(checkItemDto.getSimpleUser().getUserName());
        // 执行更新操作
        return this.checkItemMapper.updateById(checkItem);
    }

    /**
     * 分页查询检查项目
     * @param checkItemDto 查询条件
     * @return 查询结果
     */
    @Override
    public DataGridView listCheckItemForPage(CheckItemDto checkItemDto) {
        // 创建分页对象
        Page<CheckItem> page = new Page<>(checkItemDto.getPageNum(), checkItemDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<CheckItem> qw = new QueryWrapper<>();
        // 封装查询条件
        // 模糊匹配检查项目名称
        qw.like(StringUtils.isNotBlank(checkItemDto.getCheckItemName()), CheckItem.COL_CHECK_ITEM_NAME, checkItemDto.getCheckItemName());
        // 模糊匹配关键字
        qw.like(StringUtils.isNotBlank(checkItemDto.getKeywords()), CheckItem.COL_KEYWORDS, checkItemDto.getKeywords());
        // 精确匹配项目类型
        qw.eq(StringUtils.isNotBlank(checkItemDto.getTypeId()), CheckItem.COL_TYPE_ID, checkItemDto.getTypeId());
        // 精确匹配状态
        qw.eq(StringUtils.isNotBlank(checkItemDto.getStatus()), CheckItem.COL_STATUS, checkItemDto.getStatus());
        // 排序  创建时间升序
        qw.orderByAsc(CheckItem.COL_CREATE_TIME);
        // 执行查询
        this.checkItemMapper.selectPage(page, qw);
        // 封装分页结果并返回
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id删除检查项目（含批量）
     * @param checkItemIds  待删除的检查项目id
     * @return 是否删除成功标志
     */
    @Override
    public int deleteCheckItemByIds(Long[] checkItemIds) {
        // 数组转集合
        List<Long> checkItemIdsList = Arrays.asList(checkItemIds);
        if (checkItemIdsList != null && checkItemIdsList.size() >0 ){
            // 执行删除操作
            return this.checkItemMapper.deleteBatchIds(checkItemIdsList);
        }
        return 0;
    }

    /**
     * 根据id查询检查项目
     * @param checkItemId 待查询的检查项目id
     * @return 查询到的结果
     */
    @Override
    public CheckItem getCheckItemById(Long checkItemId) {
        // 执行查询操作
        return this.checkItemMapper.selectById(checkItemId);
    }

    /**
     * 查询所有可用的检查项目
     * @return 查询结果
     */
    @Override
    public List<CheckItem> selectAllCheckItem() {
        // 创建查询对象
        CheckItem checkItem = new CheckItem();
        // 封装查询条件
        QueryWrapper<CheckItem> qw = new QueryWrapper<>();
        // 状态可用
        qw.eq(CheckItem.COL_STATUS, Constants.STATUS_TRUE);
        // 执行查询，并返回结果
        return this.checkItemMapper.selectList(qw);
    }

    /**
     * 添加检查项目
     * @param checkItemDto  待添加的内容
     * @return 是否添加成功标志
     */
    @Override
    public int addCheckItem(CheckItemDto checkItemDto) {
        // 创建检查项目实体
        CheckItem checkItem = new CheckItem();
        // 值拷贝
        BeanUtil.copyProperties(checkItemDto, checkItem);
        // 设置创建人和创建时间
        checkItem.setCreateBy(checkItemDto.getSimpleUser().getUserName());
        checkItem.setCreateTime(DateUtil.date());
        // 执行插入操作
        return this.checkItemMapper.insert(checkItem);
    }
}
