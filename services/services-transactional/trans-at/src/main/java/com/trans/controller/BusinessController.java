package com.trans.at.controller;

import com.trans.at.e2e.E2EUtil;
import com.trans.at.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author finger
 */
@RestController
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @RequestMapping("/purchase")
    public void purchase(String userId, String commodityCode, int orderCount){
        String res =  "{\"res\": \"success\"}";
        try {
            businessService.purchase(userId, commodityCode, orderCount);
            if (E2EUtil.isInE2ETest()) {
                E2EUtil.writeE2EResFile(res);
            }
        } catch (Exception e) {
            if (E2EUtil.isInE2ETest() && "random exception mock!".equals(e.getMessage())) {
                E2EUtil.writeE2EResFile(res);
            }
        }
    }
}
