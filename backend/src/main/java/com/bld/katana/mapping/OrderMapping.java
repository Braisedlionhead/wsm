package com.bld.katana.mapping;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bld.constant.OrderStatusConst;
import com.bld.entity.Order;
import com.bld.entity.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapping {


    @Mapping(target = "status", expression = "java(OrderMapping.statusToDto(order.getStatus()))")
    OrderDTO toDTO(Order order);


    @Mapping(target = "status", expression = "java(OrderMapping.statusToEntity(orderDTO.getStatus()))")
    Order toEntity(OrderDTO orderDTO);


    // new
    static String statusToDto(Integer status){
        return status != null && status !=0 ? OrderStatusConst.fromCode(status).getDescription() : "等待中";
    }
    static Integer statusToEntity(String status){
        return StringUtils.isNotBlank(status) ? OrderStatusConst.fromDescription(status).getCode() : 1;
    }


}
