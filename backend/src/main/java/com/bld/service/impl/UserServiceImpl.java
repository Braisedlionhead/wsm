package com.bld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.User;
import com.bld.entity.User4Team;
import com.bld.exception.PasswordIncorrectException;
import com.bld.exception.UserNotFoundException;
import com.bld.katana.mapper.UserMapper;
import com.bld.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    UserMapper userMapper;


    @Override
    public IPage<User> pageC(IPage<User> page) {
        return userMapper.pageC(page);
    }

    @Override
    public User findByUserName(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);
        return userMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public User login(String userName, String password) {
        User loginUser = findByUserName(userName);
        if (loginUser == null) throw new UserNotFoundException("执行登陆操作：【" + userName + "】该用户不存在");

        if (!loginUser.getPassword().equals(password)){
            log.info("执行登陆操作：【" + userName + "】该用户密码输入错误");
            throw new PasswordIncorrectException("执行登陆操作：【" + userName + "】密码错误");
        }

        // 进行token的生成，存储，返回……
        // redis的话还需要对应的key

        return loginUser;
    }

    @Override
    public void logout() {
        // 登出删除token（存在的情况下
    }


    @Override
    public List<User4Team> getNoTeamUser(Integer limit) {
        return userMapper.getNoTeamUser(limit);
    }

    @Override
    public boolean updateUserByTeamId(Integer teamId, List<Integer> userIds) {
        for (Integer userId : userIds) {
            User user = userMapper.selectById(userId);
            if (user != null) {
                user.setTeamId(teamId);
                userMapper.updateById(user);
            } else {
                throw new UserNotFoundException("User with id " + userId + " not found");
            }
        }
        return true;
    }

}
