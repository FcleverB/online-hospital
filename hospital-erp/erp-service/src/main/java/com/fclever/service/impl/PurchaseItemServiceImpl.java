package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.PurchaseItem;
import com.fclever.mapper.PurchaseItemMapper;
import com.fclever.service.PurchaseItemService;
/**
@author Fclever
@create 2020-12-14 22:30
*/
@Service
public class PurchaseItemServiceImpl extends ServiceImpl<PurchaseItemMapper, PurchaseItem> implements PurchaseItemService{

}
