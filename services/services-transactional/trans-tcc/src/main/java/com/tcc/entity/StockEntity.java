package com.tcc.entity;

import lombok.Data;

/**
 * @author finger
 */
@Data
public class StockEntity {
    private long id;
    //C00321
    private String commodityCode;
    private int count;;

    private String txId;
}
