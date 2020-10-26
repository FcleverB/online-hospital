package com.fclever.vo;

import com.fclever.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Fclever
 * @create 2020-10-22 08:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser implements Serializable {

    private User user;

    private List<String> roles = Collections.EMPTY_LIST; // 用户所拥有的角色，默认为空

    private List<String> permissions = Collections.EMPTY_LIST; // 用户所拥有的权限，默认为空
}
