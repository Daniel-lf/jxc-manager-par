package com.xxxx.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoodsQuery extends BaseQuery {
    private String goodsName;
    private Integer typeId;

    private List<Integer> typeIds;

    /**
     * 用于区分库存量是否大于0的查询
     * 1  库存量=0
     * 2  库存量>0
     */
    private Integer type;
}
