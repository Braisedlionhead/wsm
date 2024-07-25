package com.bld.controller;

import com.bld.common.Result;
import com.bld.entity.User;
import com.bld.exception.PasswordIncorrectException;
import com.bld.exception.UserNotFoundException;
import com.bld.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@RestController
public class IndexController {

    @Resource
    UserService userService;

    @PostMapping("login")
    public Result login(@RequestBody User user, BindingResult bindingResult){
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(error -> log.error("User validation error occur when login---: {} ",error.getDefaultMessage()));
        if(allErrors.size() > 0){
            return Result.fail();
        }
        // 2、执行登录逻辑
        User loginUser = null;
        try{
            loginUser = userService.login(user.getUserName(),user.getPassword());
        }catch (UserNotFoundException | PasswordIncorrectException e){
            e.printStackTrace();
            return Result.fail();
        }
        System.out.println("loginUser.getRoleId() = " + loginUser.getRoleId());
        // 异常处理？？
        return Result.success(loginUser);
    }


    @PostMapping("logout")
    public Result logout(){
        try {
            userService.logout();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
        return Result.success();
    }

}
