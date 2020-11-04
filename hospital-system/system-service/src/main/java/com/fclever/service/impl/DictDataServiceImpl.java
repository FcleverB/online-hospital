package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.dto.DictDataDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.DictData;
import com.fclever.mapper.DictDataMapper;
import com.fclever.service.DictDataService;
/**
@author Fclever
@create 2020-11-03 20:11
*/
@Service
public class DictDataServiceImpl implements DictDataService{

    @Autowired
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 分页查询字典数据
     * @param dictDataDto 查询条件
     * @return 封装的查询条件
     */
    @Override
    public DataGridView listPage(DictDataDto dictDataDto) {
        // 分页  默认第一页，每页10条记录
        Page<DictData> page=new Page<>(dictDataDto.getPageNum(),dictDataDto.getPageSize());
        // 拼接查询参数
        QueryWrapper<DictData> qw=new QueryWrapper<>();
        // 字典类型相等（页面中字典名称）
        qw.eq(StringUtils.isNotBlank(dictDataDto.getDictType()),DictData.COL_DICT_TYPE,dictDataDto.getDictType());
        // 字典标签模糊查询
        qw.like(StringUtils.isNotBlank(dictDataDto.getDictLabel()),DictData.COL_DICT_LABEL,dictDataDto.getDictLabel());
        // 字典数据状态精确匹配
        qw.eq(StringUtils.isNotBlank(dictDataDto.getStatus()),DictData.COL_STATUS,dictDataDto.getStatus());
        // 执行查询，查询后数据存放于page中
        this.dictDataMapper.selectPage(page,qw);
        // 封装分页数据
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 插入新的字典数据
     *
     * @param dictDataDto 待插入数据
     * @return 插入成功的标志
     */
    @Override
    public int insert(DictDataDto dictDataDto) {
        // 持久化实体对象
        DictData dictData=new DictData();
        // 拷贝数据   源--》目标
        BeanUtil.copyProperties(dictDataDto,dictData);
        //设置创建者名称，创建时间
        dictData.setCreateBy(dictDataDto.getSimpleUser().getUserName());
        dictData.setCreateTime(DateUtil.date());
        // 执行插入操作
        return this.dictDataMapper.insert(dictData);
    }

    /**
     * 根据id修改字典数据
     *
     * @param dictDataDto 待修改数据
     * @return  修改成功的标志
     */
    @Override
    public int update(DictDataDto dictDataDto) {
        // 持久化实体对象
        DictData dictData=new DictData();
        // 拷贝属性  源--》目标
        BeanUtil.copyProperties(dictDataDto,dictData);
        //设置修改人名称
        dictData.setUpdateBy(dictDataDto.getSimpleUser().getUserName());
        // 根据id执行修改操作
        return this.dictDataMapper.updateById(dictData);
    }

    /**
     * 根据ID删除字典数据
     *
     * @param dictCodeIds 待删除的字典数据主键
     * @return 删除成功的标志
     */
    @Override
    public int deleteDictDataByIds(Long[] dictCodeIds) {
        // 数组转化为集合
        List<Long> ids= Arrays.asList(dictCodeIds);
        // 判断不为空并且长度大于0
        if(null != ids && ids.size() > 0){
            // 根据字典id批量删除字典数据
            return this.dictDataMapper.deleteBatchIds(ids);
        }else{
            // 返回-1，表示id为空
            return -1;
        }
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 待查询的字典类型
     * @return 查询到的字典数据集合
     */
    @Override
    public List<DictData> selectDictDataByDictType(String dictType) {
        // 之前是从数据库里面根据字典类型来获取对应的字典数据，现在在字典类型实现类中进行了缓存同步，就直接根据key从缓存中获取就可以了
//        QueryWrapper<DictData> qw = new QueryWrapper<>();
//        // 既然根据类型去查，类型肯定不为空
//        qw.eq(DictData.COL_DICT_TYPE, dictType);
//        // 设置排序
//        qw.orderByAsc(DictData.COL_DICT_SORT);
//        // 有效  数据
//        qw.eq(DictData.COL_STATUS, Constants.STATUS_TRUE);
//        return this.dictDataMapper.selectList(qw);
        // 设置从Redis中获取的前缀
        String key = Constants.DICT_REDIS_PROFIX + dictType;
        // 获取Redis中保存的键值对数据
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        // 根据key获取json值对象
        String json = opsForValue.get(key);
        // 将JSON对象转换为数组对象，根据DictData实体类进行解析
        List<DictData> dictDataList = JSON.parseArray(json, DictData.class);
        return dictDataList;
    }

    /**
     * 根据ID查询一个字典数据
     *
     * @param dictCode 字典数据主键
     * @return 查询到的字典数据
     */
    @Override
    public DictData selectDictDataById(Long dictCode) {
        return this.dictDataMapper.selectById(dictCode);
    }
}
