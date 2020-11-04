package com.fclever.service;

import com.fclever.domain.DictType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.DictTypeDto;
import com.fclever.vo.DataGridView;

/**
@author Fclever
@create 2020-11-02 17:47
*/
public interface DictTypeService{

        /**
         * 分页查询字典类型
         *
         * @param dictTypeDto 查询添加使用的封装类
         * @return 分页查询封装类
         */
        DataGridView listPage(DictTypeDto dictTypeDto);

        /**
         * 查询所有字典类型
         *
         * @return 分页查询封装类
         */
        DataGridView list();

        /**
         * 检查字典类型是否存在
         *
         * @param dictId 字典类型主键id
         * @param dictType  字典类型
         * @return 是否存在的标志
         */
        Boolean checkDictTypeUnique(Long dictId, String dictType);

        /**
         * 插入新的字典类型
         *
         * @param dictTypeDto 查询添加使用的封装类
         * @return 是否插入成功标志
         */
        int insert(DictTypeDto dictTypeDto);

        /**
         * 修改字典类型
         *
         * @param dictTypeDto 待修改的内容
         * @return 是否修改成功标志
         */
        int update(DictTypeDto dictTypeDto);

        /**
         * 根据ID删除字典类型
         *
         * @param dictIds 待批量删除的字典类型主键
         * @return 是否删除成功的标志
         */
        int deleteDictTypeByIds(Long[] dictIds);

        /**
         * 根据ID查询一个字典类型
         *
         * @param dictId 待查询的字典类型id
         * @return 查询到的字典类型
         */
        DictType selectDictTypeById(Long dictId);

        /**
         * 同步字典类型数据+字典数据到缓存，其他菜单模块查询字典数据直接从缓存获取
         */
        void dictCacheAsync();
}
