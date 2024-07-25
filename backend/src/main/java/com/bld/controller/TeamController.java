package com.bld.controller;

import com.bld.common.Result;
import com.bld.entity.Order;
import com.bld.entity.Team;
import com.bld.entity.dto.TeamDto;
import com.bld.katana.mapper.TeamMapper;
import com.bld.service.OrderService;
import com.bld.service.TeamService;
import com.bld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("team")
public class TeamController {


    @Resource
    TeamMapper teamMapper;

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @PostMapping("getTeamCounts")
    public Result getTeamCounts(){
        return Result.success(teamMapper.selectCount(null));
    }

    @PostMapping("getFreeTeamId")
    public Result getFreeTeamId(){
        return Result.success(teamService.getFreeTeamId());
    }

    @PostMapping("addTeam")
    public Result addTeam(@RequestBody TeamDto teamDto){
        Team newTeam = new Team();
        teamService.save(newTeam);
        Integer newTeamTeamId = newTeam.getTeamId();

        userService.updateUserByTeamId(newTeamTeamId, teamDto.getTeamMembersId());

        Order newOrder = Order.builder().partId(teamDto.getPartId())
                .quantity(teamDto.getQuantity())
                .deadline(teamDto.getDeadline())
                .teamId(newTeamTeamId)
                .status(1)
                .build();
        return orderService.save(newOrder) ? Result.success("添加成功") : Result.fail();
    }


    @PostMapping("getWorkingTeams")
    public Result getWorkingTeams(){
        return Result.success(teamService.getWorkingTeams());
    }

}
