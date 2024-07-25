package com.bld.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@TableName("part")
@ApiModel(value = "Part对象", description = "")
public class Part implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("零部件ID")
    @TableId(value = "part_id", type = IdType.AUTO)
    private Integer partId;

    @ApiModelProperty("零部件名称")
    @TableField("part_name")
    private String partName;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("成本")
    @TableField("cost")
    private BigDecimal cost;

    @ApiModelProperty("供应商ID")
    @TableField("supplier_id")
    private Integer supplierId;

    @ApiModelProperty("库存")
    @TableField("stock")
    private Integer stock;

}
