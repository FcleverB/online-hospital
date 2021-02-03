package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.OrderCharge;
import com.fclever.mapper.OrderChargeMapper;
import com.fclever.service.impl.OrderChargeService;
/**
@author Fclever
@create 2021-02-03 19:53
*/
@Service
public class OrderChargeServiceImpl extends ServiceImpl<OrderChargeMapper, OrderCharge> implements OrderChargeService{

}
