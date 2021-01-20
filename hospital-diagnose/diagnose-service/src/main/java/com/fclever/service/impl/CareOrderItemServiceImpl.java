package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.CareOrderItemMapper;
import com.fclever.domain.CareOrderItem;
import com.fclever.service.CareOrderItemService;
/**
@author Fclever
@create 2021-01-20 20:43
*/
@Service
public class CareOrderItemServiceImpl extends ServiceImpl<CareOrderItemMapper, CareOrderItem> implements CareOrderItemService{

}
