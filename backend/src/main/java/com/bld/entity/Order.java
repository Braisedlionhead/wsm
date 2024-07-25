package com.bld.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ii
 * @since 2024-05-11 10:50:47
 */

@Data
@Builder
@Accessors(chain = true)
@TableName("`order`") // mysql中的保留关键字order
@ApiModel(value = "Order对象", description = "")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty("零部件ID")
    @TableField("part_id")
    private Integer partId;

    @ApiModelProperty("数量")
    @TableField("quantity")
    private Integer quantity;

    @ApiModelProperty("截止日期")
    @TableField("deadline")
    private Date deadline;

    @ApiModelProperty("状态(1-等待、2-进行、3-完成、4-取消、-1-出错")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("负责的组号")
    @TableField("team_id")
    private Integer teamId;

    @ApiModelProperty("订单创建者")
    @TableField("creator")
    private String creator;
}
