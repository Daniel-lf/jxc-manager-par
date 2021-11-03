package com.xxxx.admin.dto;

import lombok.Data;

@Data
public class BaseQuery {
    private Integer page = 1;
    private Integer limit = 10;
}
