package com.bld.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @ApiModelProperty("用户名")
    @TableField("user_name") // 不对xml文件里的内容生效
    private String userName;

    @ApiModelProperty("昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("密码")
    @TableField("password")
//    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 返回给前端的时候不显示
    private String password;

    @ApiModelProperty("年龄")
    @TableField("age")
    private Integer age;

    @ApiModelProperty("性别")
    @TableField("sex")
    private String sex;

    @ApiModelProperty("电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("角色 0超级管理员 1管理员 2普通账号")
    @TableField("role_id")
    private Integer roleId;

    @ApiModelProperty("是否有效 1有效 其余无效")
    @TableField("is_valid")
    private Integer isValid;


    @ApiModelProperty("用户所在组别的id(0表示无所在组")
    @TableField("team_id")
    private Integer teamId;
}
