package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.CareOrder;
import com.fclever.mapper.CareOrderMapper;
import com.fclever.service.CareOrderService;
/**
@author Fclever
@create 2021-01-20 20:42
*/
@Service
public class CareOrderServiceImpl extends ServiceImpl<CareOrderMapper, CareOrder> implements CareOrderService{

}
