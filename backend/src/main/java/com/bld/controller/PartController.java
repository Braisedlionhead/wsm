package com.bld.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bld.common.DeliveryParam;
import com.bld.common.QueryPageParam;
import com.bld.common.Result;
import com.bld.constant.OrderStatusConst;
import com.bld.entity.Order;
import com.bld.entity.Part;
import com.bld.katana.mapper.PartMapper;
import com.bld.service.OrderService;
import com.bld.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("part")
public class PartController {

    @Resource
    private PartMapper partMapper;

    @Autowired
    private PartService partService;

    @Autowired
    private OrderService orderService;

    @PostMapping("list")
    public Result list() {
        return Result.success(partService.list());
    }

    @PostMapping("listPagePart")
    public Result listPage(@RequestBody QueryPageParam query) {
        IPage<Part> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<Part> result = partService.pageC(page);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @PostMapping("likePagePart")
    public Result LikePage(@RequestBody QueryPageParam queryPageParam){
        IPage<Part> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());
        LambdaQueryWrapper<Part> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Part::getPartName,queryPageParam.getParam().get("partName"));
        IPage<Part> result = partService.page(page, lambdaQueryWrapper);
        return Result.success(result.getRecords(), result.getTotal());
    }



    @PostMapping("delivery")
    public Result Delivery(@RequestBody DeliveryParam deliveryParam){
        List<Integer> orderIds = deliveryParam.getOrderIds();
        Integer partId = deliveryParam.getPartId();
        Part part = partService.getById(partId);
        Integer totalOrderQuantity = orderIds.stream()
                .map(orderId -> orderService.getById(orderId).getQuantity())
                .reduce(0, Integer::sum);
        if (part.getStock() < totalOrderQuantity) {
            return Result.fail("库存不足");
        }
        part.setStock(part.getStock() - totalOrderQuantity);
        partService.updateById(part);
        orderIds.forEach(orderId -> {
            Order order = orderService.getById(orderId);
            order.setStatus(OrderStatusConst.COMPLETED.getCode());
            orderService.updateById(order);
        });
        return Result.success();
    }

}
