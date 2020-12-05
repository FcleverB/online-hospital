package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.dto.RegisteredItemDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.RegisteredItemMapper;
import com.fclever.domain.RegisteredItem;
import com.fclever.service.RegisteredItemService;
/**
@author Fclever
@create 2020-12-05 16:18
*/
@Service
public class RegisteredItemServiceImpl implements RegisteredItemService{

    @Autowired
    private RegisteredItemMapper registeredItemMapper;

    /**
     * 修改挂号信息
     * @param registeredItemDto 待修改数据
     * @return 是否修改成功标志
     */
    @Override
    public int updateRegisteredItem(RegisteredItemDto registeredItemDto) {
        // 创建RegisteredItem对象
        RegisteredItem registeredItem = new RegisteredItem();
        // 值拷贝
        BeanUtil.copyProperties(registeredItemDto, registeredItem);
        // 设置更新人
        registeredItem.setUpdateBy(registeredItemDto.getSimpleUser().getUserName());
        // 执行更新操作
        return this.registeredItemMapper.updateById(registeredItem);
    }

    /**
     * 分页查询所有挂号信息
     * @param registeredItemDto 查询条件信息
     * @return 查询结果
     */
    @Override
    public DataGridView listRegisteredItemForPage(RegisteredItemDto registeredItemDto) {
        // 创建分页对象
        Page<RegisteredItem> page = new Page<>(registeredItemDto.getPageNum(), registeredItemDto.getPageSize());
        // 创建查询条件
        QueryWrapper<RegisteredItem> qw = new QueryWrapper<>();
        // 封装查询条件
        // 模糊匹配挂号项目名称
        qw.like(StringUtils.isNotBlank(registeredItemDto.getRegItemName()), RegisteredItem.COL_REG_ITEM_NAME, registeredItemDto.getRegItemName());
        // 精确匹配状态
        qw.eq(StringUtils.isNotBlank(registeredItemDto.getStatus()), RegisteredItem.COL_STATUS, registeredItemDto.getStatus());
        // 排序
        qw.orderByAsc(RegisteredItem.COL_CREATE_TIME);
        // 执行查询
        this.registeredItemMapper.selectPage(page, qw);
        // 封装分页对象
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 查询所有可用的挂号信息
     * @return 查询结果
     */
    @Override
    public List<RegisteredItem> selectAllRegisteredItem() {
        // 创建查询条件对象
        QueryWrapper<RegisteredItem> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(RegisteredItem.COL_STATUS, Constants.STATUS_TRUE);
        return this.registeredItemMapper.selectList(qw);
    }

    /**
     * 根据id删除对应挂号信息（含批量）
     * @param regItemId 待删除的挂号id
     * @return 是否删除成功的标志
     */
    @Override
    public int deleteRegiteredItemByIds(Long[] regItemId) {
        // 转集合
        List<Long> regItemIdsList = Arrays.asList(regItemId);
        // 判断并执行批量删除方法
        if (regItemIdsList != null && regItemIdsList.size() >0){
            return this.registeredItemMapper.deleteBatchIds(regItemIdsList);
        }
        return 0;
    }

    /**
     * 根据id查询对应的挂号信息
     * @param regItemId 要查询的挂号id
     * @return 查询结果
     */
    @Override
    public RegisteredItem getRegisteredItemById(Long regItemId) {
        return this.registeredItemMapper.selectById(regItemId);
    }

    /**
     * 添加挂号费用信息
     * @param registeredItemDto 待添加的信息
     * @return 是否添加成功的标志
     */
    @Override
    public int addRegisteredItem(RegisteredItemDto registeredItemDto) {
        // 创建RegisteredItem对象
        RegisteredItem registeredItem = new RegisteredItem();
        // 值拷贝
        BeanUtil.copyProperties(registeredItemDto, registeredItem);
        // 设置创建人和创建时间
        registeredItem.setCreateBy(registeredItemDto.getSimpleUser().getUserName());
        registeredItem.setCreateTime(DateUtil.date());
        // 执行插入方法
        return this.registeredItemMapper.insert(registeredItem);
    }
}
