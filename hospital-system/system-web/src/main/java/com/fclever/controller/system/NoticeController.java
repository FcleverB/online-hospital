package com.fclever.controller.system;

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

    @PutMapping("updateNotice")
    public AjaxResult updateNotice(@Validated NoticeDto noticeDto){
        // 设置当前登录人，用作修改人
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.noticeService.updateNotice(noticeDto));
    }

    @GetMapping("listNoticeForPage")
    public AjaxResult listNoticeForPage(NoticeDto noticeDto){
        DataGridView list = this.noticeService.listNoticeForPage(noticeDto);
        return AjaxResult.success("分页查询数据成功", list.getData(),list.getTotal());
    }

    @DeleteMapping("deleteNoticeByIds/{noticeIds}")
    public AjaxResult deleteNoticeByIds(@PathVariable Long[] noticeIds){
        return AjaxResult.toAjax(this.noticeService.deleteNoticeByIds(noticeIds));
    }

    @GetMapping("getNoticeById/{noticeId}")
    public AjaxResult getNoticeById(@PathVariable Long noticeId){
        Notice notice = this.noticeService.getNoticeById(noticeId);
        return AjaxResult.success("查询指定通知公告信息成功", notice);
    }

    @PostMapping("addNotice")
    public AjaxResult addNotice(NoticeDto noticeDto){
        // 设置当前登录人，用作创建人
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.noticeService.addNotice(noticeDto));
    }
}
