package com.xixi.mall.rbac.controller;

import com.xixi.mall.common.core.aop.PackResponseEnhance;
import com.xixi.mall.common.core.dto.MenuDto;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import com.xixi.mall.rbac.service.web.MenuService;
import com.xixi.mall.rbac.vo.MenuSimpleVo;
import com.xixi.mall.rbac.vo.MenuVo;
import com.xixi.mall.rbac.vo.RouteVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "菜单接口")
@RequestMapping(value = "/menu")
public class MenuController {

    @Resource
    private MenuService service;

    @GetMapping(value = "/route")
    @ApiOperation(value = "路由菜单", notes = "获取当前登陆用户可用的路由菜单列表")
    public ServerResponse<List<RouteVo>> route(Integer sysType) {
        return PackResponseEnhance.enhance(() -> service.route(sysType));
    }

    @GetMapping("/getMenuId")
    @ApiOperation(value = "获取菜单管理", notes = "根据menuId获取菜单管理")
    public ServerResponse<MenuVo> getByMenuId(@RequestParam Long menuId) {
        return PackResponseEnhance.enhance(() -> service.getByMenuId(menuId));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存菜单管理", notes = "保存菜单管理")
    public ServerResponse<Void> save(@Valid @RequestBody MenuDto menuDto) {
        return PackResponseEnhance.enhance(() -> service.save(menuDto));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新菜单管理", notes = "更新菜单管理")
    public ServerResponse<Void> update(@Valid @RequestBody MenuDto menuDto) {
        return PackResponseEnhance.enhance(() -> service.update(menuDto));
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除菜单管理", notes = "根据菜单管理id删除菜单管理")
    public ServerResponse<Void> delete(@RequestParam("menuId") Long menuId, @RequestParam("sysType") Integer sysType) {
        return PackResponseEnhance.enhance(() -> service.delete(menuId, sysType));
    }

    @GetMapping(value = "/listWithPermissions")
    @ApiOperation(value = "菜单列表和按钮列表", notes = "根据系统类型获取该系统的菜单列表 + 菜单下的权限列表")
    public ServerResponse<List<MenuSimpleVo>> listWithPermissions() {
        return PackResponseEnhance.enhance(() -> service.listWithPermissions());
    }

    @GetMapping(value = "/listMenuIds")
    @ApiOperation(value = "获取当前用户可见的菜单ids", notes = "获取当前用户可见的菜单id")
    public ServerResponse<List<Long>> listMenuIds() {
        return PackResponseEnhance.enhance(() -> service.listMenuIds());
    }

}
