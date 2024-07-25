package com.bld.common;


import lombok.Data;

import java.util.HashMap;

@Data
public class QueryPageParam {

    // 默认值
    private static Integer PAGE_SIZE = 20;
    private static Integer PAGE_NUM = 1;


    // 字段
    private Integer pageSize=PAGE_SIZE;
    private Integer pageNum=PAGE_NUM;

    private HashMap param = new HashMap();



}
