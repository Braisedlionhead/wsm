package com.bld.common;

import lombok.Data;

import java.util.List;


@Data
public class DeliveryParam {

    private Integer partId;
    private List<Integer> orderIds;

}
