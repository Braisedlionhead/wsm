package com.bld.constant;


import lombok.Getter;

@Getter
public enum OrderStatusConst {

    ERROR(-1, "异常"),
    WAITING(1, "等待中"),
    PROGRESSING(2, "进行中"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final int code;
    private final String description;

    OrderStatusConst(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrderStatusConst fromCode(int code) {
        for (OrderStatusConst status : OrderStatusConst.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return WAITING;
    }

    public static OrderStatusConst fromDescription(String description) {
        for (OrderStatusConst status : OrderStatusConst.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        return WAITING;
    }

}