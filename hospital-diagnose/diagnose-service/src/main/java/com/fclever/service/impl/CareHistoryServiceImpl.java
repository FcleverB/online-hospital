package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.CareHistory;
import com.fclever.mapper.CareHistoryMapper;
import com.fclever.service.CareHistoryService;
/**
@author Fclever
@create 2021-01-20 20:40
*/
@Service
public class CareHistoryServiceImpl extends ServiceImpl<CareHistoryMapper, CareHistory> implements CareHistoryService{

}
