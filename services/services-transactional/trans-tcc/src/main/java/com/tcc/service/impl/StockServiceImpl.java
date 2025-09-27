package com.tcc.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.tcc.entity.StockEntity;
import com.tcc.seata.ResultHolder;
import com.tcc.service.StockService;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author finger
 */
@Service
public class StockServiceImpl implements StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    // TODO 加上防悬挂逻辑

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean prepare(BusinessActionContext context, @BusinessActionContextParameter(paramName = "stock") StockEntity entity) {
        String txId = context.getXid();
        LOGGER.info("TccActionOne prepare, txId:{}, count:{}",txId,entity.getCount());
        Assert.isTrue(StringUtils.isNotBlank(txId), "事务开启失败");
        //扣减库存
        jdbcTemplate.update("update stock_tbl set count = count - ? where commodity_code = ?",
                new Object[]{entity.getCount(), entity.getCommodityCode()});
        entity.setTxId(txId);
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
        //回复库存
        Object obj= context.getActionContext("stock");
        if(!ObjectUtils.isEmpty(obj)){
            Gson gson = new Gson();
            StockEntity entity = gson.fromJson(obj.toString(), StockEntity.class);
            jdbcTemplate.update("update stock_tbl set count = count + ? where commodity_code = ?",
                    new Object[]{entity.getCount(), entity.getCommodityCode()});
            LOGGER.error("TccActionOne rollback, xid:{}, stock:{}",xid, context.getActionContext("stock"));
            ResultHolder.setActionOneResult(xid, "R");
        }

        return true;
    }
}
