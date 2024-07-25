package com.bld.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bld.common.QueryPageParam;
import com.bld.common.Result;
import com.bld.constant.OrderStatusConst;
import com.bld.entity.Order;
import com.bld.entity.OrderStatus;
import com.bld.entity.dto.OrderDTO;
import com.bld.katana.mapper.OrderMapper;
import com.bld.service.OrderService;
import com.bld.katana.mapping.OrderMapping;
import com.bld.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("order")
public class OrderController {


    @Resource
    private OrderMapper orderMapper;


    @Autowired
    private OrderService orderService;

    @Autowired
    private TeamService teamService;

    @Resource
    private OrderMapping orderMapping;

    @PostMapping("list")
    public Result list() {
        return Result.success(orderService.list());
    }


    @PostMapping("showStatus")
    public Result showStatus() {
        List<Integer> statusList = orderMapper.getStatusList();
        int error = 0;
        int waiting = 0;
        int processing = 0;
        int completed = 0;

        Map<OrderStatusConst, Long> statusCount = statusList.stream()
                .map(OrderStatusConst::fromCode)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        error = statusCount.getOrDefault(OrderStatusConst.ERROR, 0L).intValue();
        waiting = statusCount.getOrDefault(OrderStatusConst.WAITING, 0L).intValue();
        processing = statusCount.getOrDefault(OrderStatusConst.PROGRESSING, 0L).intValue();
        completed = statusCount.getOrDefault(OrderStatusConst.COMPLETED, 0L).intValue();


        OrderStatus orderStatus = OrderStatus.builder().
                error(error).
                waiting(waiting).
                processing(processing).
                completed(completed).
                build();
        orderStatus.updateTotal();
        return Result.success(orderStatus);
    }


    @PostMapping("listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        IPage<Order> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<OrderDTO> result = orderService.pageC(page);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @PostMapping("listPageByStatusAndTeam")
    public Result listPageByStatusAndTeam(@RequestBody QueryPageParam query) {
        IPage<Order> page = new Page<>(query.getPageNum(), query.getPageSize());
        HashMap param = query.getParam();
        String status = (String) param.get("status");
        Object teamId = param.get("teamId");
        Integer teamIdNum = teamId == null ? null : teamId instanceof Integer ? (Integer) teamId : Integer.parseInt((String) teamId);
        IPage<OrderDTO> result = orderService.pageByStatusAndTeam(page, status, teamIdNum);
        return Result.success(result.getRecords(), result.getTotal());
    }


    // new
    @PostMapping("add")
    public Result add(@RequestBody OrderDTO orderDTO) {
        Order entity = orderMapping.toEntity(orderDTO);
        teamService.updateIsBusyStatus(entity.getTeamId(), 1);
        return orderService.save(entity) ? Result.success() : Result.fail();
    }

    @PostMapping("modify")
    public Result mod(@RequestBody OrderDTO orderDTO) {
        Order entity = orderMapping.toEntity(orderDTO);
        Integer originalTeamId = orderService.getOriginalTeamId(entity.getOrderId());
        if (originalTeamId != entity.getTeamId()) {
            teamService.updateIsBusyStatus(originalTeamId, 0);
            teamService.updateIsBusyStatus(entity.getTeamId(), 1);
        }
        return orderService.updateById(entity) ? Result.success() : Result.fail();
    }



    @PostMapping("delete")
    public Result del(@RequestBody Integer orderId) {
        return orderService.removeById(orderId) ? Result.success() : Result.fail();
    }


    @PostMapping("findByPartId")
    public Result findByPartId(@RequestBody Integer partId) {
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Order::getPartId, partId);
        lambdaQueryWrapper.ne(Order::getStatus, OrderStatusConst.COMPLETED.getCode());
        List<Order> orders = orderService.list(lambdaQueryWrapper);
        return Result.success(orders);
    }



}
