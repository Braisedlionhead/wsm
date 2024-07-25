package com.bld.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.entity.Part;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PartService extends IService<Part> {

    IPage<Part> pageC(IPage<Part> page);
}
