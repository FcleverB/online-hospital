package com.fclever.service;

import com.fclever.domain.CheckResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.CheckResultDto;
import com.fclever.dto.CheckResultFormDto;
import com.fclever.vo.DataGridView;

/**
@author Fclever
@create 2021-02-15 13:11
*/
public interface CheckResultService{

    /**
     * 开始检查的方法
     * @param checkResult   待保存的数据
     */
    void startCheck(CheckResult checkResult);

    /**
     * 查询所有检查中的项目
     * @param checkResultDto    查询条件
     * @return  返回结果
     */
    DataGridView queryAllCheckingResultForPage(CheckResultDto checkResultDto);

    /**
     * 完成检查
     * @param checkResultFormDto    待更新的数据
     * @return  返回结果
     */
    int completeCheckResult(CheckResultFormDto checkResultFormDto);

    /**
     * 查询所有检查项目（检查中和检查完成）
     * @param checkResultDto    查询条件
     * @return  返回结果
     */
    DataGridView queryAllCheckResultForPage(CheckResultDto checkResultDto);
}
