package com.bld.katana.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.Part;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PartMapper extends BaseMapper<Part> {

    IPage<Part> pageC(IPage<Part> page);
}
