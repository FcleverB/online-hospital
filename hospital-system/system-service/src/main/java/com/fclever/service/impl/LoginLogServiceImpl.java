package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.dto.LoginLogDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.LoginLog;
import com.fclever.mapper.LoginLogMapper;
import com.fclever.service.LoginLogService;
/**
@author Fclever
@create 2020-11-19 08:29
*/
@Service
public class LoginLogServiceImpl implements LoginLogService{

    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     *  插入登录日志
     * @param loginLog 登录日志对象
     * @return 是否插入成功标志
     */
    @Override
    public int insertLoginLog(LoginLog loginLog) {
        return this.loginLogMapper.insert(loginLog);
    }

    /**
     * 分页查询登录日志信息
     * @param loginLogDto 封装查询条件
     * @return 分页对象，包含数据等
     */
    @Override
    public DataGridView listForPage(LoginLogDto loginLogDto) {
        // 创建分页对象  设置当前页和每页总条数
        Page<LoginLog> page = new Page<>(loginLogDto.getPageNum(), loginLogDto.getPageSize());
        // 构建查询对象
        QueryWrapper<LoginLog> qw = new QueryWrapper<>();
        // 封装查询条件   查询条件为字符串用IsNotBlank   查询条件为对应手动写！=null
        // 用户名称模糊匹配
        qw.like(StringUtils.isNotBlank(loginLogDto.getUserName()), LoginLog.COL_USER_NAME, loginLogDto.getUserName());
        // 用户账号模糊匹配
        qw.like(StringUtils.isNotBlank(loginLogDto.getLoginAccount()), LoginLog.COL_LOGIN_ACCOUNT, loginLogDto.getLoginAccount());
        // IP地址模糊匹配
        qw.like(StringUtils.isNotBlank(loginLogDto.getIpAddr()), LoginLog.COL_IP_ADDR, loginLogDto.getIpAddr());
        // 登录状态精确匹配
        qw.eq(StringUtils.isNotBlank(loginLogDto.getLoginStatus()), LoginLog.COL_LOGIN_STATUS, loginLogDto.getLoginStatus());
        // 登录类型精确匹配
        qw.eq(StringUtils.isNotBlank(loginLogDto.getLoginType()), LoginLog.COL_LOGIN_TYPE, loginLogDto.getLoginType());
        // 创建时间范围匹配
        qw.ge(loginLogDto.getBeginTime() != null, LoginLog.COL_LOGIN_TIME, loginLogDto.getBeginTime());
        qw.le(loginLogDto.getEndTime() != null, LoginLog.COL_LOGIN_TIME, loginLogDto.getEndTime());
        // 排序 按登录
        qw.orderByDesc(LoginLog.COL_LOGIN_TIME);
        // 执行查询方法
        this.loginLogMapper.selectPage(page, qw);
        // 构建分页查询结果的封装类   数据总条数+查询到的数据
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id删除登录日志信息（含批量）
     * @param infoIds 待删除的登录日志id
     * @return 是否删除成功标记
     */
    @Override
    public int deleteLoginLogByIds(Long[] infoIds) {
        List<Long> infoList = Arrays.asList(infoIds);
        if (infoList.size() > 0){
            return this.loginLogMapper.deleteBatchIds(infoList);
        }
        return 0;
    }

    /**
     * 清空所有登录日志
     * @return 是否清空成功的标志
     */
    @Override
    public int clearAllLoginLog() {
        return this.loginLogMapper.delete(null);
    }
}
