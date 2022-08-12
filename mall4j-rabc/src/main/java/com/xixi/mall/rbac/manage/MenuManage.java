package com.xixi.mall.rbac.manage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xixi.mall.common.cache.constant.CacheNames;
import com.xixi.mall.rbac.entity.MenuEntity;
import com.xixi.mall.rbac.mapper.MenuMapper;
import com.xixi.mall.rbac.vo.MenuSimpleVo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuManage {

    @Resource
    private MenuMapper menuMapper;

    @CacheEvict(cacheNames = CacheNames.MENU_LIST_KEY, key = "#menuEntity.bizType")
    public void save(MenuEntity menuEntity) {
        menuMapper.insert(menuEntity);
    }

    @CacheEvict(cacheNames = CacheNames.MENU_LIST_KEY, key = "#menuEntity.bizType")
    public void update(MenuEntity menuEntity) {
        menuMapper.updateById(menuEntity);
    }

    @CacheEvict(cacheNames = CacheNames.MENU_LIST_KEY, key = "#sysType")
    public void deleteById(Long menuId, Integer sysType) {
        menuMapper.delete(
                Wrappers.<MenuEntity>lambdaQuery()
                        .eq(MenuEntity::getMenuId, menuId)
                        .eq(MenuEntity::getBizType, sysType)
        );
    }

    @Cacheable(cacheNames = CacheNames.MENU_LIST_KEY, key = "#sysType")
    public List<MenuEntity> listBySysType(Integer sysType) {
        return menuMapper.selectList(
                Wrappers.<MenuEntity>lambdaQuery()
                        .eq(MenuEntity::getBizType, sysType)
                        .orderByAsc(MenuEntity::getCreateTime)
                        .orderByAsc(MenuEntity::getSeq)
        );
    }

    public List<MenuSimpleVo> listWithPermissions(Integer sysType) {
//        select m.menu_id,m.parent_id,m.title,mp.name,mp.menu_permission_id, mp.menu_id permission_menu_id
//        from menu m
//        left join menu_permission mp on m.menu_id = mp.menu_id
//        where m.biz_type = #{sysType}
        return menuMapper.listWithPermissions();
    }
}
