package com.bld.katana.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    IPage<Order> pageC(IPage<Order> page);

    @Select("select status from `order`")
    List<Integer> getStatusList();
}
