package com.bld.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bld.entity.dto.OrderDTO;

public interface OrderService extends IService<Order> {

    IPage<OrderDTO> pageC(IPage<Order> page);

    IPage<OrderDTO> pageByStatusAndTeam(IPage<Order> page, String status, Integer teamId);
    // new DTO

    Integer getOriginalTeamId(Integer orderId);

}
