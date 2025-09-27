package com.tcc.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.tcc.entity.StockEntity;
import com.tcc.seata.ResultHolder;
import com.tcc.service.StockRedisService;
import com.tcc.utils.RedisUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * @author finger
 */
@Service
public class StockServiceRedisImpl implements StockRedisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceRedisImpl.class);

    // TODO 加上防悬挂逻辑

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean prepare(BusinessActionContext context, @BusinessActionContextParameter(paramName = "stock") StockEntity stockEntity) {
        String txId = context.getXid();
        LOGGER.info("TccActionOne prepare, txId:{}, count:{}",txId,stockEntity.getCount());
        Assert.isTrue(StringUtils.isNotBlank(txId), "事务开启失败");
        //Redis 扣减库存
        Object value = redisUtil.get(stockEntity.getCommodityCode());
        if(ObjectUtils.isEmpty(value)){
            redisUtil.set(stockEntity.getCommodityCode(), stockEntity.getCount());
        }
        redisUtil.set(stockEntity.getCommodityCode(),(int) value - stockEntity.getCount());
        stockEntity.setTxId(txId);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext context) {
        String xid = context.getXid();
        Assert.isTrue(context.getActionContext("stock") != null,"stock must not be null");
        LOGGER.info("TccActionOne commit, xid:{}, stock:{}" ,xid, context.getActionContext("stock"));
        ResultHolder.setActionOneResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext context) {
        String xid = context.getXid();
        Assert.isTrue(context.getActionContext("stock") != null,"stock must not be null");
        //Redis 恢复库存
        Object obj= context.getActionContext("stock");
        if(!ObjectUtils.isEmpty(obj)){
            Gson gson = new Gson();
            StockEntity entity = gson.fromJson(obj.toString(), StockEntity.class);
            int result = (int) redisUtil.get(entity.getCommodityCode());
            redisUtil.set(entity.getCommodityCode(), result + entity.getCount());
            LOGGER.error("TccActionOne rollback, xid:{}, stock:{}" ,xid, context.getActionContext("stock"));
            ResultHolder.setActionOneResult(xid, "R");
        }
        return true;
    }
}
