package com.tcc.service.impl;

import com.tcc.entity.StockEntity;
import com.tcc.service.StockRedisService;
import com.tcc.service.StockService;
import com.tcc.service.TccTransactionService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author finger
 */
@Service
public class TccTransactionServiceImpl implements TccTransactionService {

    @Resource
    private StockService stockService;

    @Resource
    private StockRedisService stockRedisService;

    @GlobalTransactional
    @Override
    public String doTransactionCommit(StockEntity entity) throws InterruptedException {
        boolean result = stockService.prepare(null, entity);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        result =stockRedisService.prepare(null, entity);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        return "success";
    }

    @GlobalTransactional
    @Override
    public void doTransactionRollback(StockEntity entity) {
        //第一个TCC 事务参与者
        boolean result = stockService.prepare(null, entity);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        result = stockRedisService.prepare(null, entity);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        throw new RuntimeException("transaction rollback");
    }
}
