package com.bld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bld.entity.Team;
import com.bld.katana.mapper.TeamMapper;
import com.bld.service.TeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {


    @Resource
    TeamMapper teamMapper;

    @Override
    public List<Integer> getFreeTeamId() {
        LambdaQueryWrapper<Team> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Team::getIsBusy, 0);
        return teamMapper.selectList(wrapper).stream().map(Team::getTeamId).collect(Collectors.toList());
    }


    @Override
    public List<Team> getWorkingTeams() {
        LambdaQueryWrapper<Team> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Team::getIsBusy, 1);
        return teamMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void updateIsBusyStatus(Integer originalTeamId, int i) {
        Team team = new Team();
        team.setIsBusy(i);
        LambdaQueryWrapper<Team> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Team::getTeamId, originalTeamId);
        teamMapper.update(team, wrapper);
    }


}
