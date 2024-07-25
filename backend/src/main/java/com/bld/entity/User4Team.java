package com.bld.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class User4Team implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String userName;
}
