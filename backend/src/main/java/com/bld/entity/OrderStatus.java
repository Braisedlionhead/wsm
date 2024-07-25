package com.bld.entity;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer error;
    private Integer waiting;
    private Integer processing;
    private Integer completed;
    private Integer total;

    // ??
    public void updateTotal() {
        total = error + waiting + processing + completed;
    }


}
