package com.bld.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bld.common.QueryPageParam;
import com.bld.common.Result;
import com.bld.entity.User;
import com.bld.katana.mapper.UserMapper;
import com.bld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @PostMapping("list")
    public Result list() {
        return Result.success(userService.list());
    }



    @PostMapping("add")
    public Result add(@RequestBody User user) {
        System.out.println(user);
        return userService.save(user)?Result.success():Result.fail();
    }


    @PostMapping("delete")
    public boolean delete(@RequestBody Integer id) {
        return userService.removeById(id);
    }


    @PostMapping("mod")
    public Result mod(@RequestBody User user) {
        System.out.println("mod");
        System.out.println(user);
        return userService.updateById(user) ? Result.success() : Result.fail();
    }

    @PostMapping("modOrAdd")
    public boolean modOrAdd(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    @PostMapping("like")
    public Result like(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(user.getUserName())){
            LambdaQueryWrapper<User> like = lambdaQueryWrapper.like(User::getUserName, user.getUserName());
        }
        return  Result.success(userService.list(lambdaQueryWrapper));
    }


    @PostMapping("listPageUser")
    public Result listPageC1(@RequestBody QueryPageParam query) {
        IPage<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<User> result = userService.pageC(page);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @PostMapping("findById")
    public Result findById(@RequestBody Integer id){
        // 对入参进行校验……（待完善
        //...
        return Result.success(userMapper.selectById(id));
    }


    @PostMapping("searchByNicknameLike")
    public Result searchByNicknameLike(@RequestBody QueryPageParam queryPageParam){
        IPage<User> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getNickName,queryPageParam.getParam().get("nickName"));
        IPage<User> result = userService.page(page, lambdaQueryWrapper);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @PostMapping("getNoTeamUser")
    public Result getNoTeamUser(@RequestBody Integer limit){
        return Result.success(userService.getNoTeamUser(limit));
    }


}
