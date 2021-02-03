package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.OrderChargeItemMapper;
import com.fclever.domain.OrderChargeItem;
import com.fclever.service.impl.OrderChargeItemService;
/**
@author Fclever
@create 2021-02-03 19:53
*/
@Service
public class OrderChargeItemServiceImpl extends ServiceImpl<OrderChargeItemMapper, OrderChargeItem> implements OrderChargeItemService{

}
