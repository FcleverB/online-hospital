package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fclever.constants.Constants;
import com.fclever.domain.CareOrder;
import com.fclever.mapper.CareOrderMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class CareOrderItemServiceImpl implements CareOrderItemService{

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    @Autowired
    private CareOrderMapper careOrderMapper;

    /**
     * 根据处方id查询对应的详细处方项目信息
     * @param coId  处方id
     * @return  返回结果
     */
    @Override
    public List<CareOrderItem> queryCareOrderItemsByCoId(String coId) {
        // 构建查询条件对象
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(coId !=null, CareOrderItem.COL_CO_ID, coId);
        // 执行查询返回结果
        return this.careOrderItemMapper.selectList(qw);
    }

    /**
     * 根据处方项目id查询对应的处方项目
     * @param itemId    处方项目id
     * @return  返回结果
     */
    @Override
    public CareOrderItem queryCareOrderItemByItemId(String itemId) {
        // 处方项目id是主键
        return this.careOrderItemMapper.selectById(itemId);
    }

    /**
     * 根据处方项目id删除对应的处方项目
     * @param itemId    待删除的处方项目
     * @return  返回结果
     */
    @Override
    public int deleteCareOrderItemByItemId(String itemId) {
        //注意点，如果删除了，要更新careOrder主表的all_amount
        // 总金额是根据该处方id下的所有处方详情信息进行计算得出的
        CareOrderItem careOrderItem=this.careOrderItemMapper.selectById(itemId);
        // 获取到对应处方主表的id
        String coId=careOrderItem.getCoId();
        //删除
        int i=this.careOrderItemMapper.deleteById(itemId);

        // 根据主表id查询出删除后剩余的所有的处方详情集合
        QueryWrapper<CareOrderItem> qw=new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID,coId);
        List<CareOrderItem> careOrderItems=this.careOrderItemMapper.selectList(qw);
        // 对该主表下的集合数据进行分析，得出新的总金额，并更新
        if(careOrderItems!=null&&careOrderItems.size()>0){
            //重新计算总价格
            BigDecimal allAmount=new BigDecimal("0");
            for (CareOrderItem orderItem : careOrderItems) {
                allAmount=allAmount.add(orderItem.getAmount());
            }
            //再根据coId查询主表的数据
            CareOrder careOrder=this.careOrderMapper.selectById(coId);
            //更新主表的数据
            careOrder.setAllAmount(allAmount);
            this.careOrderMapper.updateById(careOrder);
        }else{
            // 如果对应的处方主表没有详情信息了，则将对应的处方主表记录删除掉
            this.careOrderMapper.deleteById(coId);
        }
        return i;
    }

    /**
     * 根据处方id，查询未支付的处方详情信息
     * @param coId  处方id
     * @return  返回结果
     */
    @Override
    public List<CareOrderItem> queryCareOrderItemsNoChargeByCoId(String coId) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(coId != null, CareOrderItem.COL_CO_ID, coId);
        qw.eq(CareOrderItem.COL_STATUS, Constants.ORDER_DETAILS_STATUS_0);
        return this.careOrderItemMapper.selectList(qw);
    }

    /**
     * 根据处方id，查询已支付的处方详情信息
     * @param coId  处方id
     * @return  返回结果
     */
    @Override
    public List<CareOrderItem> queryCareOrderItemsChargedByCoId(String coId) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(coId != null, CareOrderItem.COL_CO_ID, coId);
        qw.eq(CareOrderItem.COL_STATUS, Constants.ORDER_DETAILS_STATUS_1);
        return this.careOrderItemMapper.selectList(qw);
    }
}
