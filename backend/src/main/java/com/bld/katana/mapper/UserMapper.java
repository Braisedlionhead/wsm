package com.bld.katana.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bld.entity.User4Team;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    IPage<User> pageC(IPage<User> page);


    List<User4Team> getNoTeamUser(Integer limit);
}
