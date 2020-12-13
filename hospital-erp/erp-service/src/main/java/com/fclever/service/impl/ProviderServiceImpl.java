package com.fclever.service.impl;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.constants.Constants;
import com.fclever.domain.Provider;
import com.fclever.dto.ProviderDto;
import com.fclever.mapper.ProviderMapper;
import com.fclever.mapper.ProviderMapper;
import com.fclever.domain.Provider;
import com.fclever.service.ProviderService;
import com.fclever.service.ProviderService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
/**
@author Fclever
@create 2020-12-13 13:43
*/
// 指定该类中的addProvider方法的请求重试次数为0
@Service(methods = {@Method(name = "addProvider", retries = 0)})
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderMapper providerMapper;

    /**
     * 修改供应商信息
     * @param providerDto 修改的供应商信息
     * @return 是否修改成功标志
     */
    @Override
    public int updateProvider(ProviderDto providerDto) {
        // 创建实体对象
        Provider provider = new Provider();
        // 值拷贝
        BeanUtil.copyProperties(providerDto, provider);
        // 设置修改人
        provider.setUpdateBy(providerDto.getSimpleUser().getUserName());
        // 执行更新操作
        return this.providerMapper.updateById(provider);
    }

    /**
     * 分页查询供应商信息
     * @param providerDto 待修改的供应商信息
     * @return 分页数据
     */
    @Override
    public DataGridView listProviderForPage(ProviderDto providerDto) {
        // 创建分页对象
        Page<Provider> page = new Page<>(providerDto.getPageNum(), providerDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<Provider> qw = new QueryWrapper<>();
        // 封装查询条件
        // 供应商名称模糊匹配
        qw.like(StringUtils.isNotBlank(providerDto.getProviderName()), Provider.COL_PROVIDER_NAME, providerDto.getProviderName());
        // 联系人模糊匹配
        qw.like(StringUtils.isNotBlank(providerDto.getContactName()), Provider.COL_CONTACT_NAME, providerDto.getContactName());
        // 联系人电话模糊匹配
        qw.like(StringUtils.isNotBlank(providerDto.getContactTel()), Provider.COL_CONTACT_TEL, providerDto.getContactTel());
        // 状态精确匹配
        qw.eq(StringUtils.isNotBlank(providerDto.getStatus()), Provider.COL_STATUS, providerDto.getStatus());
        // 根据创建时间升序排序
        qw.orderByAsc(Provider.COL_CREATE_TIME);
        // 执行查询
        this.providerMapper.selectPage(page, qw);
        // 封装分页对象并返回结果
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id删除供应商（含批量）
     * @param providerIds 待删除的供应商id集合
     * @return 是否删除成功标志
     */
    @Override
    public int deleteProviderByIds(Long[] providerIds) {
        List<Long> providerIdsList = Arrays.asList(providerIds);
        if (providerIdsList != null && providerIdsList.size() > 0){
            return this.providerMapper.deleteBatchIds(providerIdsList);
        }
        return 0;
    }

    /**
     * 根据id查询对应的供应商信息
     * @param providerId 待查询的供应商id
     * @return 查询结果
     */
    @Override
    public Provider getProviderById(Long providerId) {
        return this.providerMapper.selectById(providerId);
    }

    /**
     * 查询所有可用的供应商信息
     * @return 查询结果
     */
    @Override
    public List<Provider> selectAllProvider() {
        // 创建查询条件对象
        QueryWrapper<Provider> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(Provider.COL_STATUS, Constants.STATUS_TRUE);
        // 执行查询
        return this.providerMapper.selectList(qw);
    }

    /**
     * 添加供应商信息
     * @param providerDto 带添加的数据
     * @return 是否添加成功标志
     */
    @Override
    public int addProvider(ProviderDto providerDto) {
        // 创建实体对象
        Provider provider = new Provider();
        // 值拷贝
        BeanUtil.copyProperties(providerDto, provider);
        // 设置创建人和创建时间
        provider.setCreateBy(providerDto.getSimpleUser().getUserName());
        provider.setCreateTime(DateUtil.date());
        // 执行插入操作
        return this.providerMapper.insert(provider);
    }
}
