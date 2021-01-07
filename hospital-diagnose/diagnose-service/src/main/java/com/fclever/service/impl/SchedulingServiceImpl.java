package com.fclever.service.impl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Scheduling;
import com.fclever.dto.SchedulingFormDto;
import com.fclever.dto.SchedulingQueryDto;
import com.fclever.mapper.SchedulingMapper;
import com.fclever.service.SchedulingService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
@author Fclever
@create 2021-01-05 12:25
*/
@Service // 更换为Dubbo的Service，作为服务交给Dubbo管理
public class SchedulingServiceImpl implements SchedulingService{

    @Autowired
    private SchedulingMapper schedulingMapper;

    /**
     * 根据条件查询符合的排班信息
     * @param schedulingQueryDto
     * @return 查询到的排班数据列表
     */
    @Override
    public List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        // 创建查询条件对象
        QueryWrapper<Scheduling> qw = new QueryWrapper<>();
        // 精确查询用户id
        qw.eq(schedulingQueryDto.getUserId() != null, Scheduling.COL_USER_ID, schedulingQueryDto.getUserId());
        // 精确查询科室id
        qw.eq(schedulingQueryDto.getDeptId() != null, Scheduling.COL_DEPT_ID, schedulingQueryDto.getDeptId());
        // 范围匹配周日期
        qw.ge(StringUtils.isNotBlank(schedulingQueryDto.getBeginDate()), Scheduling.COL_SCHEDULING_DAY, schedulingQueryDto.getBeginDate());
        qw.le(StringUtils.isNotBlank(schedulingQueryDto.getEndDate()), Scheduling.COL_SCHEDULING_DAY, schedulingQueryDto.getEndDate());
        // 执行查询List
        return this.schedulingMapper.selectList(qw);
    }

    /**
     * 保存排班信息
     * @param schedulingFormDto  待保存的排班数据  传递的是Json对象
     * @return 返回结果
     */
    @Override
    public int saveScheduling(SchedulingFormDto schedulingFormDto) {
        //保存的条件
        if(null!=schedulingFormDto.getData()&&schedulingFormDto.getData().size()>0){
            DateTime dateTime = DateUtil.parse(schedulingFormDto.getBeginDate(), "yyyy-MM-dd");
            //得到dateTime所在周的开始和结束日期
            DateTime beginWeek = DateUtil.beginOfWeek(dateTime);
            DateTime endWeek = DateUtil.endOfWeek(dateTime);
            // 周开始和结束时间进行格式化
            String beginDate=DateUtil.format(beginWeek,"yyyy-MM-dd");
            String endDate = DateUtil.format(endWeek, "yyyy-MM-dd");
            //得到用户名和科室,从一条数据中获取即可
            SchedulingFormDto.SchedulingData schedulingData = schedulingFormDto.getData().get(0);
            Long userId = schedulingData.getUserId();
            Long deptId = schedulingData.getDeptId();
            // 不为空表示数据库中存在原有数据
            if(null!=userId){
                //删除原来这个用户的当前周的所有排班数据
                QueryWrapper<Scheduling> qw=new QueryWrapper<>();
                qw.eq(Scheduling.COL_USER_ID,userId);
                qw.eq(Scheduling.COL_DEPT_ID,deptId);
                // 时间范围当前周所有
                qw.between(Scheduling.COL_SCHEDULING_DAY,beginDate,endDate);
                this.schedulingMapper.delete(qw);
                //再进行保存添加新排班
//                初始化当前周 周一到周日的日期数据
                List<String> schedulingDays=initSchedulingDay(beginWeek);
                for (SchedulingFormDto.SchedulingData dataForm : schedulingFormDto.getData()) {
                    Scheduling scheduling=null;
                    int i = 0;//记录循环次数取日期值
                    for (String s : dataForm.getSchedulingType()) {
                        if(StringUtils.isNotBlank(s)){
                            scheduling=new Scheduling(userId,deptId,schedulingDays.get(i),dataForm.getSubsectionType(),s,DateUtil.date(),schedulingFormDto.getSimpleUser().getUserName());
                            //保存
                            this.schedulingMapper.insert(scheduling);
                        }
                        i++;
                    }
                }
                return 1;//保存成功
            }else{
                return 0;
            }
        }
        return 0;
    }

    /**
     *根据一周的开始时间根据周一到周日的日期数组
     * @param date
     * @return
     */
    private List<String> initSchedulingDay(DateTime date) {
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 7 ; i++) {
            DateTime dateTime = DateUtil.offsetDay(date, i);
            list.add(DateUtil.format(dateTime,"yyyy-MM-dd"));
        }
        return list;
    }
}
