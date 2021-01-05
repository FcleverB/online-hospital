package com.fclever.controller.doctor;

import com.fclever.controller.BaseController;
import com.fclever.domain.User;
import com.fclever.service.UserService;
import com.fclever.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 排班控制层
 * @author Fclever
 * @create 2021-01-05 11:31
 */
@RestController
@RequestMapping("doctor/scheduling")
public class SchedulingController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 查询可以进行排班的医生列表(下拉框)
     *      如果deptId为空,那么就表示查询全部需要排班的医生列表
     *      如果deptId不为空,那么就查询对应部门下的需要排班的医生列表
     * @return 返回结果
     */
    @GetMapping("queryUsersNeedScheduling")
    public AjaxResult queryUsersNeedScheduling(Long deptId) {
        List<User> users = this.userService.queryUsersNeedScheduling(null, deptId);
        return AjaxResult.success(users);
    }
}
