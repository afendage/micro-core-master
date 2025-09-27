package com.tcc.controller;

import com.tcc.entity.StockEntity;
import com.tcc.service.TccTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author finger
 */
@RestController
public class BusinessController {

    @Autowired
    TccTransactionService tccTransactionService;

    @PostMapping("/deductTcc")
    public String deductTcc(@RequestBody StockEntity entity) throws InterruptedException {
        return tccTransactionService.doTransactionCommit(entity);
    }

    @PostMapping("/deductTccRollback")
    public void deductTccRollback(@RequestBody StockEntity entity){
        tccTransactionService.doTransactionRollback(entity);
    }

}
