package com.bld.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.Part;
import com.bld.katana.mapper.PartMapper;
import com.bld.service.PartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class PartServiceImpl extends ServiceImpl<PartMapper, Part> implements PartService {

    @Resource
    PartMapper partMapper;

    @Override
    public IPage<Part> pageC(IPage<Part> page) {
        return partMapper.pageC(page);
    }
}
