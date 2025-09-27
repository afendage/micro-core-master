package com.tcc.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.tcc.entity.StockEntity;
import com.tcc.seata.ResultHolder;
import com.tcc.service.StockService;
import com.tcc.utils.RedisUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author finger
 */
@Service
public class StockServiceImpl implements StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    RedisUtil redisUtil;

    // 放悬挂 KEY
    private  String  TRY="TRY:",  ROLLBACK="ROLLBACK:";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean prepare(BusinessActionContext context, @BusinessActionContextParameter(paramName = "stock") StockEntity entity) {
        String txId = context.getXid();
        LOGGER.info("TccActionOne prepare, txId:{}, count:{}",txId,entity.getCount());
        Assert.isTrue(StringUtils.isNotBlank(txId), "事务开启失败");
        // 防悬挂
        Object obj= redisUtil.get(ROLLBACK+txId);
        if(!ObjectUtils.isEmpty(obj)){
            throw new RuntimeException("ROLLBACK 先执行了");
        }
        //扣减库存
        jdbcTemplate.update("update stock_tbl set count = count - ? where commodity_code = ?",
                new Object[]{entity.getCount(), entity.getCommodityCode()});
        entity.setTxId(txId);
        // 防悬挂
        redisUtil.setWithExpire(TRY+txId, "success", 1,TimeUnit.DAYS);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext context) {
        String xid = context.getXid();
        Assert.isTrue(context.getActionContext("stock") != null,"stock must not be null");
        LOGGER.info("TccActionOne commit, xid:{}, stock:{}" ,xid, context.getActionContext("stock"));
        ResultHolder.setActionOneResult(xid, "T");
        // 提交成功可以清楚 放悬挂redis（可选）
        //redisUtil.delete(TRY+xid);
        //redisUtil.delete(ROLLBACK+xid);
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext context) {
        String txId = context.getXid();
        Assert.isTrue(context.getActionContext("stock") != null,"stock must not be null");
        // 放悬挂：如果需要可以判断 try 阶段是否已经执行
        // redisUtil.get(ROLLBACK+txId)
        //回复库存
        Object obj= context.getActionContext("stock");
        if(!ObjectUtils.isEmpty(obj)){
            Gson gson = new Gson();
            StockEntity entity = gson.fromJson(obj.toString(), StockEntity.class);
            jdbcTemplate.update("update stock_tbl set count = count + ? where commodity_code = ?",
                    new Object[]{entity.getCount(), entity.getCommodityCode()});
            LOGGER.error("TccActionOne rollback, xid:{}, stock:{}",txId, context.getActionContext("stock"));
            ResultHolder.setActionOneResult(txId, "R");
        }
        // 防悬挂
        redisUtil.setWithExpire(ROLLBACK+txId, "success", 1,TimeUnit.DAYS);
        return true;
    }
}
