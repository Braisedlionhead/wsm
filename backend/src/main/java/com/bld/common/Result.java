package com.bld.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回给前端的数据的封装类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private Integer code; //200-成功 400-失败

    private String msg; // 成功  失败

    private Long total; // 总记录数

    private Object data; //数据

    private static Result result(Integer code, String msg, Long total, Object object){
        return new Result(code, msg, total, object);
    }

    public static Result fail(){
        return result(400, "failed", 0L, null);
    }

    public static Result fail(String msg){
        return result(400, msg, 0L, null);
    }

    public static Result success(){
        return result(200, "success", 0L, null);
    }

    public static Result success(Object data){
        return result(200, "success", 0L, data); // ?0L:1L;
    }

    public static Result success( Object data,Long total){
        return result(200, "success", total, data);
    }
}
