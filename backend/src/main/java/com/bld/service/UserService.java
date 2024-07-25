package com.bld.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bld.entity.User4Team;

import java.util.List;


public interface UserService extends IService<User> {

    IPage<User> pageC(IPage<User> page);

    User findByUserName(String userName);

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    User login(String userName, String password);


    void logout();

    List<User4Team> getNoTeamUser(Integer limit);

    boolean updateUserByTeamId(Integer teamId, List<Integer> userIds);

}
