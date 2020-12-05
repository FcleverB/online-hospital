package com.fclever.controller.system;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.domain.Notice;
import com.fclever.dto.NoticeDto;
import com.fclever.service.NoticeService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 通知公告控制层
 * @author Fclever
 * @create 2020-12-03 10:49
 */
@RestController
@RequestMapping("system/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 修改通知公告
     * @param noticeDto 待修改的数据
     * @return 修改成功标志
     */
    @PutMapping("updateNotice")
    @Log(title = "修改通知公告",businessType = BusinessType.UPDATE)
    public AjaxResult updateNotice(@Validated NoticeDto noticeDto){
        // 设置当前登录人，用作修改人
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.noticeService.updateNotice(noticeDto));
    }

    /**
     * 分页查询所有通知公告
     * @param noticeDto 查询条件
     * @return 返回结果
     */
    @GetMapping("listNoticeForPage")
    public AjaxResult listNoticeForPage(NoticeDto noticeDto){
        DataGridView list = this.noticeService.listNoticeForPage(noticeDto);
        return AjaxResult.success("分页查询数据成功", list.getData(),list.getTotal());
    }

    /**
     * 根据id删除通知公告（含批量）
     * @param noticeIds 待删除的通知公告id数组
     * @return 是否删除成功标志
     */
    @DeleteMapping("deleteNoticeByIds/{noticeIds}")
    @Log(title = "根据id删除通知公告（含批量）",businessType = BusinessType.DELETE)
    public AjaxResult deleteNoticeByIds(@PathVariable Long[] noticeIds){
        return AjaxResult.toAjax(this.noticeService.deleteNoticeByIds(noticeIds));
    }

    /**
     * 根据id获取指定通知公告信息
     * @param noticeId 查询的通知id
     * @return 查询到的数据
     */    
    @GetMapping("getNoticeById/{noticeId}")
    public AjaxResult getNoticeById(@PathVariable Long noticeId){
        Notice notice = this.noticeService.getNoticeById(noticeId);
        return AjaxResult.success("查询指定通知公告信息成功", notice);
    }

    /**
     * 添加通知公告信息
     * @param noticeDto 待添加内容
     * @return 是否添加成功标志
     */
    @PostMapping("addNotice")
    @Log(title = "添加通知公告信息",businessType = BusinessType.INSERT)
    public AjaxResult addNotice(NoticeDto noticeDto){
        // 设置当前登录人，用作创建人
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.noticeService.addNotice(noticeDto));
    }
}
