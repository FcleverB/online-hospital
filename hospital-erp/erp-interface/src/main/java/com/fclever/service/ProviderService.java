package com.fclever.service;

import com.fclever.domain.Provider;
import com.fclever.domain.Provider;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.ProviderDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-12-13 13:43
*/
public interface ProviderService {

    /**
     * 修改供应商信息
     * @param providerDto 修改的供应商信息
     * @return 是否修改成功标志
     */
    int updateProvider(ProviderDto providerDto);

    /**
     * 分页查询供应商信息
     * @param providerDto 待修改的供应商信息
     * @return 分页数据
     */
    DataGridView listProviderForPage(ProviderDto providerDto);

    /**
     * 根据id删除供应商（含批量）
     * @param providerIds 待删除的供应商id集合
     * @return 是否删除成功标志
     */
    int deleteProviderByIds(Long[] providerIds);

    /**
     * 根据id查询对应的供应商信息
     * @param providerId 待查询的供应商id
     * @return 查询结果
     */
    Provider getProviderById(Long providerId);

    /**
     * 查询所有可用的供应商信息
     * @return 查询结果
     */
    List<Provider> selectAllProvider();

    /**
     * 添加供应商信息
     * @param providerDto 带添加的数据
     * @return 是否添加成功标志
     */
    int addProvider(ProviderDto providerDto);
}
