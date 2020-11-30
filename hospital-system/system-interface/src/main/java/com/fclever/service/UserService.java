package com.fclever.service;

import com.fclever.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.UserDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-10-20 13:13
*/
public interface UserService{

    /**
     * 根据手机号查询用户
     *      用户登录是通过手机号和密码
     *      shiro验证：先通过手机号判断用户是否存在，如果存在则继续判断密码正确与否
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 分页查询用户信息
     * @param userDto 查询条件
     * @return 查询结果
     */
    DataGridView listUserForPage(UserDto userDto);

    /**
     * 添加用户信息
     * @param userDto 待添加数据
     * @return 插入成功标志
     */
    int addUser(UserDto userDto);

    /**
     * 根据用户id查询用户
     *      登录之后需要获取用户相关信息，基于token，也就需要用id来查询
     * @param userId
     * @return 查询结果
     */
    User getUserById(Long userId);

    /**
     * 修改用户信息
     * @param userDto 待修改数据
     * @return 修改成功标志
     */
    int updateUser(UserDto userDto);

    /**
     * 根据id删除用户信息（含批量）
     * @param userIds 待删除的用户id数组
     * @return 是否删除成功标志
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 重置用户密码
     * @param userIds 待重置密码的用户id数组
     * @return 是否重置成功的标志
     */
    int resetPassword(Long[] userIds);

    /**
     * 查询所有用户信息（可用）
     * @return 查询结果
     */
    List<User> selectAllUser();
}
