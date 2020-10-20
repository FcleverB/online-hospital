package com.fclever.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 构造菜单并返回给前端
 * @author Fclever
 * @create 2020-10-20 08:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {

    // 菜单id
    private String id;

    // 菜单对应的url
    private String serPath;

    // 是否显示该菜单
    private boolean show = true;

    public MenuTreeVo(String id, String serPath) {
        this.id = id;
        this.serPath = serPath;
    }
}
