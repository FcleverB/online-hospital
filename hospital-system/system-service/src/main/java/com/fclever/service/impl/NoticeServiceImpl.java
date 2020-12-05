package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.dto.NoticeDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Notice;
import com.fclever.mapper.NoticeMapper;
import com.fclever.service.NoticeService;
/**
@author Fclever
@create 2020-12-02 23:39
*/
@Service
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 修改通知公告
     * @param noticeDto 待修改的数据
     * @return 修改成功标志
     */
    @Override
    public int updateNotice(NoticeDto noticeDto) {
        // 创建通知实体类
        Notice notice = new Notice();
        // 值拷贝
        BeanUtil.copyProperties(noticeDto, notice);
        // 设置更新人
        notice.setUpdateBy(noticeDto.getSimpleUser().getUserName());
        //执行更新操作
        return this.noticeMapper.updateById(notice);
    }

    /**
     * 分页查询所有通知公告
     * @param noticeDto 查询条件
     * @return 查询结果
     */
    @Override
    public DataGridView listNoticeForPage(NoticeDto noticeDto) {
        // 创建分页对象
        Page<Notice> page = new Page<>(noticeDto.getPageNum(), noticeDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<Notice> qw = new QueryWrapper<>();
        // 封装查询条件
        // 模糊匹配通知公告标题
        qw.like(StringUtils.isNotBlank(noticeDto.getNoticeTitle()), Notice.COL_NOTICE_TITLE, noticeDto.getNoticeTitle());
        // 模糊匹配创建者
        qw.like(StringUtils.isNotBlank(noticeDto.getCreateBy()), Notice.COL_CREATE_BY, noticeDto.getCreateBy());
        // 精确匹配公告类型
        qw.eq(StringUtils.isNotBlank(noticeDto.getNoticeType()), Notice.COL_NOTICE_TYPE, noticeDto.getNoticeType());
        // 排序  创建时间升序
        qw.orderByAsc(Notice.COL_CREATE_TIME);
        // 执行查询
        this.noticeMapper.selectPage(page, qw);
        // 封装分页并返回
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id删除通知公告（含批量）
     * @param noticeIds 待删除的通知公告id数组
     * @return 是否删除成功标志
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        int row = 0;
        List<Long> noticeIdsList = Arrays.asList(noticeIds);
        if (noticeIdsList != null && noticeIdsList.size() > 0 ){
            row += this.noticeMapper.deleteBatchIds(noticeIdsList);
        }
        return row;
    }

    /**
     * 根据id获取指定通知公告信息
     * @param noticeId 查询的通知id
     * @return 查询到的数据
     */
    @Override
    public Notice getNoticeById(Long noticeId) {
        return this.noticeMapper.selectById(noticeId);
    }

    /**
     * 添加通知公告信息
     * @param noticeDto 待添加内容
     * @return 是否添加成功标志
     */
    @Override
    public int addNotice(NoticeDto noticeDto) {
        // 创建Notice实体
        Notice notice = new Notice();
        // 值拷贝
        BeanUtil.copyProperties(noticeDto, notice);
        // 设置创建人和创建时间
        notice.setCreateBy(noticeDto.getSimpleUser().getUserName());
        notice.setCreateTime(DateUtil.date());
        // 执行保存
        return this.noticeMapper.insert(notice);
    }
}
