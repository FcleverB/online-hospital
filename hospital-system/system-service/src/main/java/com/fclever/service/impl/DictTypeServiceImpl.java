package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.dto.DictTypeDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fclever.mapper.DictTypeMapper;
import com.fclever.domain.DictType;
import com.fclever.service.DictTypeService;

import java.util.Arrays;
import java.util.List;

/**
@author Fclever
@create 2020-11-02 17:47
*/
@Service
public class DictTypeServiceImpl implements DictTypeService{

    @Autowired
    private DictTypeMapper dictTypeMapper;

    /**
     * 分页查询字典类型
     * @param dictTypeDto 查询添加使用的封装类
     * @return
     */
    @Override
    public DataGridView listPage(DictTypeDto dictTypeDto) {
        // Mybatisplus   设置分页默认值，默认为第一页，条数为10
        Page<DictType> page = new Page<>(dictTypeDto.getPageNum(), dictTypeDto.getPageSize());
        // 封装查询条件
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        // 先判断是否字典名称是否为空，如果不为空，则将val作为对应表字段进行模糊like查询
        qw.like(StringUtils.isNotBlank(dictTypeDto.getDictName()), DictType.COL_DICT_NAME, dictTypeDto.getDictName());
        qw.like(StringUtils.isNotBlank(dictTypeDto.getDictType()), DictType.COL_DICT_TYPE, dictTypeDto.getDictType());
        // 等于匹配，非空后，做精确匹配
        qw.eq(StringUtils.isNotBlank(dictTypeDto.getStatus()), DictType.COL_STATUS, dictTypeDto.getStatus());
        // 范围匹配，创建日期（开始时间----结束时间）
        // 大于等于
        qw.ge(dictTypeDto.getBeginTime() != null, DictType.COL_CREATE_TIME, dictTypeDto.getBeginTime());
        // 小于等于
        qw.le(dictTypeDto.getEndTime() != null, DictType.COL_CREATE_TIME, dictTypeDto.getEndTime());
        // 根据pw查询，并将数据保存到page中
        // 调用Mapper执行selectPage(分页查询），条件为qw，保存分页相关信息和查询数据到page中
        this.dictTypeMapper.selectPage(page, qw);
        // 构建分页查询的封装类   数据总条数+查询到的数据
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 查询所有字典类型    处于可用状态的字典类型
     * @return 分页查询封装类
     */
    @Override
    public DataGridView list() {
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        // 查询条件，状态为0，可用的状态
        qw.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null, this.dictTypeMapper.selectList(qw));
    }

    /**
     * 检查字典类型是否存在
     *      比如在添加或者修改等操作时，需要验证该字典类型是否存在，这时候就会调用
     *      添加操作时，dictId为空，
     *      修改操作时，需要注意修改后的
     * @param dictId 字典类型主键id
     * @param dictType  字典类型
     * @return 是否存在的标志
     */
    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        // 如果dictId为null，表示为增加操作，那么给它一个-1，数据库id自增， 所以不会出现相同，否则为更新操作
        dictId = (dictId == null) ? -1L : dictId;
        // 查询条件：查询字典类型等于选择的字典类型
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        qw.eq(DictType.COL_DICT_TYPE, dictType);
        // 执行查询
        DictType dbDictType = this.dictTypeMapper.selectOne(qw);
        /**
         * 如果是添加操作，默认id为null，因为通过数据库自动递增，如果根据待添加的类型能在数据库查到数据
         *      就说明该种字典类型已经存在了，不需要重复添加，返回false，停止该操作
         *
         *  如果是更新操作，默认id为对应记录的id，根据类型能查询到结果，并且id相同，就返回true，可以进行更新操作
          */
        // 如果能根据字典类型查询到一条结果，并且查询到的id不相等，那就说明数据库不存在
        if (null != dbDictType && dictId.longValue() != dbDictType.getDictId().longValue()){
            return true; // 说明不存在
        }
        return false; // 说明存在
    }

    /**
     * 插入新的字典类型
     * @param dictTypeDto 查询添加使用的封装类
     * @return 是否插入成功的标志
     */
    @Override
    public int insert(DictTypeDto dictTypeDto) {
        // 形参为数据传输实体类，但是调用Mapper进行持久化需要使用原本实体类
        DictType dictType = new DictType();
        // 两个实体属性对应，将值进行拷贝  使用hutools工具类
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        dictType.setCreateTime(DateUtil.date());
        return this.dictTypeMapper.insert(dictType);
    }

    /**
     * 修改字典类型
     * @param dictTypeDto 待修改的内容
     * @return 是否修改成功的标志
     */
    @Override
    public int update(DictTypeDto dictTypeDto) {
        // 形参为数据传输实体类，但是调用Mapper进行持久化需要使用原本实体类
        DictType dictType = new DictType();
        // 设置修改人
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setUpdateBy(dictTypeDto.getSimpleUser().getUserName());
        return this.dictTypeMapper.updateById(dictType);
    }

    /**
     * 根据ID删除字典类型
     * @param dictIds 待批量删除的字典类型主键
     * @return 是否删除成功的标志
     */
    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        // 数组转集合
        List<Long> ids = Arrays.asList(dictIds);
        // 如果不为空，并且长度大于0，调用Mapper进行批量删除
        if (null != ids && ids.size() > 0){
            return this.dictTypeMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    /**
     * 根据ID查询一个字典类型
     * @param dictId 待查询的字典类型id
     * @return 查询到的字典类型
     */
    @Override
    public DictType selectDictTypeById(Long dictId) {
        return this.dictTypeMapper.selectById(dictId);
    }
}
