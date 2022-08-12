package com.xixi.mall.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xixi.mall.rbac.entity.MenuEntity;
import com.xixi.mall.rbac.vo.MenuSimpleVo;

import java.util.List;

/**
 * @author FrozenWatermelon
 */
public interface MenuMapper extends BaseMapper<MenuEntity> {

    List<MenuSimpleVo> listWithPermissions();

}
