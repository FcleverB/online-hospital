package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.dto.UserDto;
import com.fclever.mapper.RoleMapper;
import com.fclever.mapper.UserMapper;
import com.fclever.utils.Md5Utils;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fclever.domain.User;
import com.fclever.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
@author Fclever
@create 2020-10-20 13:13
*/
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据手机号查询用户
     *      用户登录是通过手机号和密码
     *      shiro验证：先通过手机号判断用户是否存在，如果存在则继续判断密码正确与否
     * @param phone
     * @return
     */
    @Override
    public User queryUserByPhone(String phone) {
        // 创建查询条件对象
        QueryWrapper<User> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(User.COL_PHONE, phone);
        // 执行查询操作
        User user = this.userMapper.selectOne(qw);
        return user;
    }

    /**
     * 分页查询用户信息
     * @param userDto 查询条件
     * @return 查询结果
     */
    @Override
    public DataGridView listUserForPage(UserDto userDto) {
        // 创建分页对象
        Page<User> page = new Page<>(userDto.getPageNum(), userDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<User> qw = new QueryWrapper<>();
        // 封装查询条件
        // 精确匹配科室名称---下拉码表，保存的是科室id
        qw.eq(userDto.getDeptId() != null,User.COL_DEPT_ID, userDto.getDeptId());
        // 模糊匹配用户名称
        qw.like(StringUtils.isNotBlank(userDto.getUserName()), User.COL_USER_NAME, userDto.getUserName());
        // 模糊匹配手机号
        qw.like(StringUtils.isNotBlank(userDto.getPhone()), User.COL_PHONE, userDto.getPhone());
        // 精确匹配状态
        qw.eq(StringUtils.isNotBlank(userDto.getStatus()), User.COL_STATUS, userDto.getStatus());
        // 范围匹配创建时间
        qw.ge(userDto.getBeginTime() != null,User.COL_CREATE_TIME, userDto.getBeginTime());
        qw.le(userDto.getBeginTime() != null,User.COL_CREATE_TIME, userDto.getEndTime());
        qw.orderByAsc(User.COL_UNION_ID);
        // 执行查询
        this.userMapper.selectPage(page, qw);
        // 封装返回数据并返回
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加用户信息
     * @param userDto 待添加数据
     * @return 插入成功标志
     */
    @Override
    public int addUser(UserDto userDto) {
        // 创建User
        User user = new User();
        // 值拷贝
        BeanUtil.copyProperties(userDto, user);
        // 设置一些默认值
        user.setUserType(Constants.USER_NORMAL); // 系统用户，超级管理员是固定的
        // 取出电话前5位，进行盐加密之后保存为密码
        String defaultPassword = user.getPhone().substring(5);
        user.setSalt(Md5Utils.createSalt());
        user.setPassword(Md5Utils.md5(defaultPassword, user.getSalt(), 2));
        // 设置创建者和创建时间
        user.setCreateBy(userDto.getSimpleUser().getUserName());
        user.setCreateTime(DateUtil.date());
        // 执行插入操作
        return this.userMapper.insert(user);
    }

    /**
     * 根据用户id查询用户
     *      登录之后需要获取用户相关信息，基于token，也就需要用id来查询
     * @param userId 待查询的用户id
     * @return 查询结果
     */
    @Override
    public User getUserById(Long userId) {
        return this.userMapper.selectById(userId);
    }

    /**
     * 修改用户信息
     * @param userDto 待修改数据
     * @return 修改成功标志
     */
    @Override
    public int updateUser(UserDto userDto) {
        // 创建User实体
        User user = new User();
        // 值拷贝
        BeanUtil.copyProperties(userDto, user);
        // 设置更新人
        user.setUpdateBy(userDto.getSimpleUser().getUserName());
        // 执行更新
        return this.userMapper.updateById(user);
    }

    /**
     * 根据id删除用户信息（含批量）
     * @param userIds 待删除的用户id数组
     * @return 是否删除成功标志
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        List<Long> userIdsList = Arrays.asList(userIds);
        if (userIdsList != null && userIdsList.size() >0 ){
            // 也要删除对应的用户--角色表数据
            this.roleMapper.deleteRoleUserByUserIds(userIdsList);
            return this.userMapper.deleteBatchIds(userIdsList);
        }
        return 0;
    }

    /**
     * 重置用户密码
     * @param userIds 待重置密码的用户id数组
     * @return 是否重置成功的标志
     */
    @Override
    public int resetPassword(Long[] userIds) {
        int row = 0;
        for (Long userId : userIds) {
            User user = this.userMapper.selectById(userId);
            String defaultPassword = "";
            // 如果是超级管理员，密码就是123456
            if (user.getUserType().equals(Constants.USER_ADMIN)){
                defaultPassword = "123456";
            }else {
                // 电话号码为11位，substring截取的是从索引为5（包括）的位置开始截取到最后
                // "Harbison".substring(3) returns "bison"
                defaultPassword = user.getPhone().substring(5);
            }
            user.setSalt(Md5Utils.createSalt());
            user.setPassword(Md5Utils.md5(defaultPassword, user.getSalt(), 2));
            row += this.userMapper.updateById(user);
        }
        return row > 0 ? row : 0;
    }

    /**
     * 查询所有用户信息（可用）
     * @return 查询结果
     */
    @Override
    public List<User> selectAllUser() {
        // 创建查询条件对象
        QueryWrapper<User> qw = new QueryWrapper<>();
        // 设置查询条件
        qw.eq(User.COL_STATUS, Constants.STATUS_TRUE);
        // 系统用户
        qw.eq(User.COL_USER_TYPE, Constants.USER_NORMAL);
        qw.orderByAsc(User.COL_USER_ID);
        // 执行查询操作并返回结果
        return this.userMapper.selectList(qw);
    }

}
