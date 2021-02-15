package com.fclever.service;

import com.fclever.domain.CheckResult;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
