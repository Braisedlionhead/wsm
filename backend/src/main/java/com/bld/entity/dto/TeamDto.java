package com.bld.entity.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TeamDto {
    private Integer partId;
    private Integer quantity;
    private Date deadline;
    private List<Integer> teamMembersId;
}
