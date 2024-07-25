package com.bld.service;

import com.bld.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface TeamService extends IService<Team> {

    List<Integer> getFreeTeamId();

    List<Team> getWorkingTeams();

    void updateIsBusyStatus(Integer originalTeamId, int i);
}
