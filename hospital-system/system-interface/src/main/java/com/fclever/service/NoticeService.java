package com.fclever.service;

import com.fclever.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.NoticeDto;
import com.fclever.vo.DataGridView;

/**
@author Fclever
@create 2020-12-02 23:39
*/
public interface NoticeService {

    /**
     * 修改通知公告
     * @param noticeDto 待修改的数据
     * @return 修改成功标志
     */
    int updateNotice(NoticeDto noticeDto);

    /**
     * 分页查询所有通知公告
     * @param noticeDto 查询条件
     * @return 查询结果
     */
    DataGridView listNoticeForPage(NoticeDto noticeDto);

    /**
     * 根据id删除通知公告（含批量）
     * @param noticeIds 待删除的通知公告id数组
     * @return 是否删除成功标志
     */
    int deleteNoticeByIds(Long[] noticeIds);

    /**
     * 根据id获取指定通知公告信息
     * @param noticeId 查询的通知id
     * @return 查询到的数据
     */
    Notice getNoticeById(Long noticeId);

    /**
     * 添加通知公告信息
     * @param noticeDto 待添加内容
     * @return 是否添加成功标志
     */
    int addNotice(NoticeDto noticeDto);
}
