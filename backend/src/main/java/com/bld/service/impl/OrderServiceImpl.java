package com.bld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bld.entity.Order;
import com.bld.entity.dto.OrderDTO;
import com.bld.katana.mapper.OrderMapper;
import com.bld.katana.mapping.OrderMapping;
import com.bld.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    OrderMapping orderMapping;



    public IPage<OrderDTO> pageC(IPage<Order> page) {
        IPage<Order> orderPage = orderMapper.pageC(page);

        List<OrderDTO> orderDTOList = orderPage.getRecords().stream()
//                .filter(Objects::nonNull)
                .map(orderMapping::toDTO)
                .collect(Collectors.toList());

        IPage<OrderDTO> orderDTOPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        orderDTOPage.setRecords(orderDTOList);

        return orderDTOPage;
    }

    @Override
    public IPage<OrderDTO> pageByStatusAndTeam(IPage<Order> page, String status, Integer teamId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            if (status != null) {
                Integer statusCode = OrderMapping.statusToEntity(status);
                queryWrapper.eq(Order::getStatus, statusCode);
            }
            if (teamId != null) {
                queryWrapper.eq(Order::getTeamId, teamId);
            }

            IPage<Order> orderPage = orderMapper.selectPage(page, queryWrapper);

            List<OrderDTO> orderDTOList = orderPage.getRecords().stream()
                    .map(orderMapping::toDTO)
                    .collect(Collectors.toList());

            IPage<OrderDTO> orderDTOPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
            orderDTOPage.setRecords(orderDTOList);

            return orderDTOPage;
    }

    @Override
    public Integer getOriginalTeamId(Integer orderId) {
        Order order = getById(orderId);
        return order.getTeamId();
    }

}
