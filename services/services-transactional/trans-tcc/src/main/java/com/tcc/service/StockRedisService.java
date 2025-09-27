package com.tcc.service;

import com.tcc.entity.StockEntity;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.stereotype.Service;

/**
 * @author finger
 */
@LocalTCC
public interface StockRedisService {

    @TwoPhaseBusinessAction(name = "stockRedisService", commitMethod = "commit", rollbackMethod = "rollback")
    boolean prepare(BusinessActionContext context, @BusinessActionContextParameter(paramName = "stock") StockEntity stockEntity);

    boolean commit(BusinessActionContext context);

    boolean rollback(BusinessActionContext context);


}
