package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.CheckItemMapper;
import com.fclever.domain.CheckItem;
import com.fclever.service.CheckItemService;
/**
@author Fclever
@create 2020-12-05 12:36
*/
@Service
public class CheckItemServiceImpl extends ServiceImpl<CheckItemMapper, CheckItem> implements CheckItemService{

}
