package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.MedicinesMapper;
import com.fclever.domain.Medicines;
import com.fclever.service.MedicinesService;
/**
@author Fclever
@create 2020-12-12 22:11
*/
@Service
public class MedicinesServiceImpl extends ServiceImpl<MedicinesMapper, Medicines> implements MedicinesService{

}
