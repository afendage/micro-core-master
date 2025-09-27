package com.tcc.service;

import com.tcc.entity.StockEntity;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface StockService {

    @TwoPhaseBusinessAction(name = "stockService", commitMethod = "commit", rollbackMethod = "rollback")
    boolean prepare(BusinessActionContext context, @BusinessActionContextParameter(paramName = "stock") StockEntity stockEntity);

    boolean commit(BusinessActionContext context);

    boolean rollback(BusinessActionContext context);


}
