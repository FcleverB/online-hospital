package com.fclever.service;

import com.fclever.domain.DictData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.DictDataDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-11-03 20:11
*/
public interface DictDataService{

    /**
     * 分页查询字典数据
     *
     * @param dictDataDto 查询条件
     * @return 分页查询封装对象  total+数据
     */
    DataGridView listPage(DictDataDto dictDataDto);


    /**
     * 插入新的字典数据
     *
     * @param dictDataDto 待插入数据
     * @return 插入成功的标志
     */
    int insert(DictDataDto dictDataDto);

    /**
     * 根据id修改字典数据
     *
     * @param dictDataDto 待修改数据
     * @return  修改成功的标志
     */
    int update(DictDataDto dictDataDto);

    /**
     * 根据ID删除字典数据
     *
     * @param dictCodeIds 待删除的数据集合
     * @return 删除成功的标志
     */
    int deleteDictDataByIds(Long[] dictCodeIds);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 待查询的字典类型
     * @return 查询到的字典数据集合
     */
    List<DictData> selectDictDataByDictType(String dictType);

    /**
     * 根据ID查询一个字典数据
     *
     * @param dictCode 字典数据主键
     * @return 查询到的字典数据
     */
    DictData selectDictDataById(Long dictCode);
}
