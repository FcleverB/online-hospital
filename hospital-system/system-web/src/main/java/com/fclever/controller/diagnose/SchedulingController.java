package com.fclever.controller.diagnose;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.controller.BaseController;
import com.fclever.domain.Scheduling;
import com.fclever.domain.User;
import com.fclever.dto.SchedulingDto;
import com.fclever.dto.SchedulingFormDto;
import com.fclever.dto.SchedulingQueryDto;
import com.fclever.service.SchedulingService;
import com.fclever.service.UserService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 排班控制层
 * @author Fclever
 * @create 2021-01-05 11:31
 */
@RestController
@RequestMapping("doctor/scheduling")
public class SchedulingController extends BaseController {

    @Reference
    private SchedulingService schedulingService;

    @Autowired
    private UserService userService;

    /**
     * 查询可以进行排班的医生列表(下拉框)
     * @param deptId
     *              如果deptId为空,那么就表示查询全部需要排班的医生列表
     *              如果deptId不为空,那么就查询对应部门下的需要排班的医生列表
     * @return 返回结果
     */
    @GetMapping("queryUsersNeedScheduling")
    public AjaxResult queryUsersNeedScheduling(Long deptId) {
        List<User> users = this.userService.queryUsersNeedScheduling(null, deptId);
        return AjaxResult.success(users);
    }

    /**
     * 条件查询可以排班的医生的排班信息（数据列表）
     */
    @GetMapping("queryScheduling")
    @HystrixCommand // 涉及到远程调用schedulingService
    public AjaxResult queryScheduling(SchedulingQueryDto schedulingQueryDto){
        // 根据科室id和用户id查询用户信息，如果用户id和科室id都为空，那么就查询所有排班的用户信息
        // 返回结果为查询到的用户（医生）信息，在根据日期范围进行查询进行数据回显即可
        // 需要查询排班信息表，然后将对应查询日期的排班信息查询出来
        List<User> users = this.userService.queryUsersNeedScheduling(schedulingQueryDto.getUserId(),schedulingQueryDto.getDeptId());
        return getSchedulingAjaxResult(schedulingQueryDto, users);
    }

    /**
     * 保存排班信息
     * @param schedulingFormDto  待保存的排班数据  传递的是Json对象
     * @return 返回结果
     */
    @PostMapping("saveScheduling")
    @HystrixCommand  //涉及到远程调用
    @Log(title = "保存排班信息",businessType = BusinessType.INSERT)
    public AjaxResult saveScheduling(@RequestBody SchedulingFormDto schedulingFormDto){
        schedulingFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.schedulingService.saveScheduling(schedulingFormDto));
    }


    /**
     *  根据条件构造页面显示数据
     * @param schedulingQueryDto    前端的查询条件
     * @param users 符合部门和用户条件的用户数据
     * @return
     */
    private AjaxResult getSchedulingAjaxResult(SchedulingQueryDto schedulingQueryDto, List<User> users) {
        // 获取当前时间
        DateTime date = DateUtil.date();
        // 判断查询条件中日期是否为空，如果不为空，则表示是进行了上一周和下一周的选择
        if (StringUtils.isNotBlank(schedulingQueryDto.getQueryDate())) {
            // 查询条件中日期不为空，将查询条件中的日期进行类型转换
            // 需要提醒一点，前端传递的日期为周一或者周日
            // 传递周一，表示点了上一周
            // 传递周日，表示点了下一周
            date = DateUtil.parse(schedulingQueryDto.getQueryDate(), "yyyy-MM-dd");
            // 根据传过来的日期判断是周几
            int i = DateUtil.dayOfWeek(date); // 1 表示周日  2 表示周一
            if (i == 1) {
                // 表示要查询下一周
                date = DateUtil.offsetDay(date, 1);
            } else {
                // 表示查询上一周
                date = DateUtil.offsetDay(date, -1);
            }
        }
        // date已经根据条件作了偏移，此时计算date所在周的周一日期和周日日期
        // 周一为开始时间   周日为结束时间
        DateTime beginTime = DateUtil.beginOfWeek(date);
        DateTime endTime = DateUtil.endOfWeek(date);
        // 保存到Dto中
        schedulingQueryDto.setBeginDate(DateUtil.format(beginTime, "yyyy-MM-dd"));
        schedulingQueryDto.setEndDate(DateUtil.format(endTime, "yyyy-MM-dd"));
        // 根据开始日期和结束日期查询数据库中对应数据
        List<Scheduling> schedulingList = this.schedulingService.queryScheduling(schedulingQueryDto);
        // 存放页面中需要显示的列表数据
        ArrayList<SchedulingDto> schedulingDtos = new ArrayList<>();
        for (User user : users) {
            // 封装某部门，某用户的一周所有上午数据
            SchedulingDto schedulingMorning = new SchedulingDto(user.getUserId(), user.getDeptId(), "1", initMap(beginTime));
            // 封装某部门，某用户的一周所有下午数据
            SchedulingDto schedulingAfternoon = new SchedulingDto(user.getUserId(), user.getDeptId(), "2", initMap(beginTime));
            // 封装某部门，某用户的一周所有晚上数据
            SchedulingDto schedulingEvening = new SchedulingDto(user.getUserId(), user.getDeptId(), "3", initMap(beginTime));
            // 一个用户对应上午下午和晚上，对应3条数据
            schedulingDtos.add(schedulingMorning);
            schedulingDtos.add(schedulingAfternoon);
            schedulingDtos.add(schedulingEvening);
            for (Scheduling scheduling : schedulingList) {
                // 从数据库中获取数据
                Long userId = scheduling.getUserId();    // 数据库中查到的userId
                String subsectionType = scheduling.getSubsectionType(); // 数据库查到的上午下午晚上的类型
                String schedulingDay = scheduling.getSchedulingDay(); // 数据库查到的值班类型  门诊和急症
                // 判断userid一致性
                if (user.getUserId().equals(userId)){
                    // 判断排版时段
                    switch (subsectionType){
                        case "1":
                            // 处理上午的排班类型
                            Map<String, String> recordMorning = schedulingMorning.getRecord();
                            // schedulingDay为日期，如果map集合里面已经有该key，则直接覆盖原有值
                            recordMorning.put(schedulingDay,scheduling.getSchedulingType());
                            break;
                        case "2":
                            Map<String, String> recordAfternoon = schedulingAfternoon.getRecord();
                            recordAfternoon.put(schedulingDay,scheduling.getSchedulingType());
                            break;
                        case "3":
                            Map<String, String> recordEvening = schedulingEvening.getRecord();
                            recordEvening.put(schedulingDay,scheduling.getSchedulingType());
                            break;
                    }
                }
            }
            schedulingMorning.setSchedulingType(schedulingMorning.getRecord().values());
            schedulingAfternoon.setSchedulingType(schedulingAfternoon.getRecord().values());
            schedulingEvening.setSchedulingType(schedulingEvening.getRecord().values());
        }
        // 组装返回的对象
        Map<Object, Object> resMap = new HashMap<>();
        resMap.put("tableData", schedulingDtos);

        Map<String, Object> schedulingData = new HashMap<>();
        schedulingData.put("startTimeWeek", schedulingQueryDto.getBeginDate());
        schedulingData.put("endTimeWeek", schedulingQueryDto.getEndDate());
        resMap.put("schedulingDate", schedulingData);
        resMap.put("labelNames", initLabel(beginTime));
        return AjaxResult.success(resMap);
    }

    /**
     * 翻译周
     *
     * @param i
     * @return
     */
    private String formatterWeek(int i) {
        switch (i) {
            case 0:
                return "(周一)";
            case 1:
                return "(周二)";
            case 2:
                return "(周三)";
            case 3:
                return "(周四)";
            case 4:
                return "(周五)";
            case 5:
                return "(周六)";
            default:
                return "(周日)";
        }
    }

    /**
     * 初始化labelNames
     *
     * @param beginTime
     * @return
     */
    private String[] initLabel(DateTime beginTime) {
        String[] labelNames = new String[7];
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.offsetDay(beginTime, i);
            labelNames[i] = DateUtil.format(d, "yyyy-MM-dd") + formatterWeek(i);
        }
        return labelNames;
    }

    /**
     * 生成值班记录Map集合
     *      key为日期
     *      value为  1和2  门诊和急诊
     * @param beginTime 目前查询周的第一天
     * @return 对应周的日期集合
     */
    private Map<String, String> initMap(DateTime beginTime) {
        // 改为LinkedHashMap，因为保存的日期是从对应周第一天到最后一天的，需要保持插入顺序一致
        Map<String, String> map = new LinkedHashMap<>();
        // 传递的beginTime为目前周的第一天，通过循环可以遍历这周的7天，将日期保存到map集合中
        for (int i = 0; i < 7; i++){
            DateTime dateTime = DateUtil.offsetDay(beginTime, i);
            String key = DateUtil.format(dateTime, "yyyy-MM-dd");
            map.put(key, "");
        }
        return map;
    }
}
