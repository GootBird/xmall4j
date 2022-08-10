package com.xixi.mall.rbac.manage;

import com.xixi.mall.rbac.mapper.MenuMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MenuManage {

    @Resource
    private MenuMapper menuMapper;


}
