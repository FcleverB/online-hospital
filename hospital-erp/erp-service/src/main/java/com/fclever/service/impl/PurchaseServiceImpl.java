package com.fclever.service.impl;

import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Purchase;
import com.fclever.mapper.PurchaseMapper;
import com.fclever.service.PurchaseService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
@author Fclever
@create 2020-12-14 15:21
*/
@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private PurchaseMapper purchaseMapper;
}
