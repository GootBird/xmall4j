package com.xixi.mall.rbac.service.web;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.common.core.dto.MenuDto;
import com.xixi.mall.common.core.utils.BooleanUtil;
import com.xixi.mall.common.core.utils.ThrowUtils;
import com.xixi.mall.common.security.context.AuthUserContext;
import com.xixi.mall.rbac.entity.MenuEntity;
import com.xixi.mall.rbac.manage.MenuManage;
import com.xixi.mall.rbac.mapper.MenuMapper;
import com.xixi.mall.rbac.vo.MenuSimpleVo;
import com.xixi.mall.rbac.vo.MenuVo;
import com.xixi.mall.rbac.vo.RouteMetaVo;
import com.xixi.mall.rbac.vo.RouteVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.xixi.mall.common.core.constant.Constant.VOID;

@Service
public class MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private MenuManage menuManage;


    public List<RouteVo> route(Integer sysType) {

        sysType = Optional.ofNullable(sysType)
                .orElse(
                        AuthUserContext.get().getSysType()
                );

        return menuManage.listBySysType(sysType)
                .stream()
                .map(menuEntity ->
                        new RouteVo()
                                .setAlwaysShow(BooleanUtil.isTrue(menuEntity.getAlwaysShow()))
                                .setComponent(menuEntity.getComponent())
                                .setHidden(BooleanUtil.isTrue(menuEntity.getHidden()))
                                .setName(menuEntity.getName())
                                .setPath(menuEntity.getPath())
                                .setRedirect(menuEntity.getRedirect())
                                .setId(menuEntity.getMenuId())
                                .setParentId(menuEntity.getParentId())
                                .setSeq(menuEntity.getSeq())
                                .setMeta(
                                        new RouteMetaVo()
                                                .setActiveMenu(menuEntity.getActiveMenu())
                                                .setAffix(BooleanUtil.isTrue(menuEntity.getAffix()))
                                                .setBreadcrumb(BooleanUtil.isTrue(menuEntity.getBreadcrumb()))
                                                .setIcon(menuEntity.getIcon())
                                                .setNoCache(BooleanUtil.isTrue(menuEntity.getNoCache()))
                                                .setTitle(menuEntity.getTitle())
                                                .setRoles(Collections.singletonList(menuEntity.getPermission()))
                                )
                )
                .collect(Collectors.toList());
    }

    public MenuVo getByMenuId(Long menuId) {
        MenuEntity menuEntity = menuMapper.selectOne(
                Wrappers.<MenuEntity>lambdaQuery()
                        .eq(MenuEntity::getMenuId, menuId)
        );
        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(menuEntity, menuVo);
        return menuVo;
    }

    public Void save(MenuDto menuDto) {
        MenuEntity menu = checkAndGenerate(menuDto);
        menu.setMenuId(null);
        menuManage.save(menu);
        return VOID;

    }

    private MenuEntity checkAndGenerate(MenuDto menuDto) {
        UserInfoInTokenBo userInfoInTokenBO = AuthUserContext.get();

        if (!Objects.equals(userInfoInTokenBO.getTenantId(), 0L)) {
            ThrowUtils.throwErr("无权限操作！");
        }

        MenuEntity menuEntity = new MenuEntity();
        BeanUtils.copyProperties(menuDto, menuEntity);

        menuEntity.setBizType(
                Optional.ofNullable(menuDto.getSysType())
                        .orElse(AuthUserContext.get().getSysType())
        );
        return menuEntity;
    }

    public Void update(MenuDto menuDto) {
        MenuEntity menuEntity = checkAndGenerate(menuDto);
        menuManage.update(menuEntity);
        return VOID;
    }

    public Void delete(Long menuId, Integer sysType) {

        UserInfoInTokenBo userInfoInTokenBo = AuthUserContext.get();

        if (!Objects.equals(userInfoInTokenBo.getTenantId(), 0L)) {
            ThrowUtils.throwErr("无权限操作！");
        }

        sysType = Objects.isNull(sysType)
                ? userInfoInTokenBo.getSysType()
                : sysType;

        menuManage.deleteById(menuId, sysType);

        return VOID;
    }

    public List<MenuSimpleVo> listWithPermissions() {

        UserInfoInTokenBo userInfoInTokenBo = AuthUserContext.get();
        List<MenuSimpleVo> menuList = menuManage.listWithPermissions(userInfoInTokenBo.getSysType());
        return menuList;
    }

    public List<Long> listMenuIds() {
        UserInfoInTokenBo userInfoInTokenBo = AuthUserContext.get();
        List<Long> menuList = menuService.listMenuIds(userInfoInTokenBo.getUserId());
        return menuList;
    }
}
