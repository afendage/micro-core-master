package com.tcc.service;

import com.tcc.entity.StockEntity;

public interface TccTransactionService {

    String doTransactionCommit(StockEntity entity) throws InterruptedException;

    void doTransactionRollback(StockEntity entity);

}
