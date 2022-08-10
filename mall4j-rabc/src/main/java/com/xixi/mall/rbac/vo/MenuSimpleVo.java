package com.xixi.mall.rbac.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 菜单管理VO
 */
@Getter
@Setter
public class MenuSimpleVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty("父菜单ID，一级菜单为0")
    private Long parentId;

    @ApiModelProperty("设置该路由在侧边栏和面包屑中展示的名字")
    private String title;

    @ApiModelProperty("菜单权限列表")
    private List<MenuPermissionSimpleVo> menuPermissions;

}
