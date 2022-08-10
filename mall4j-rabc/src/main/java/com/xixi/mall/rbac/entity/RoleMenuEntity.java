package com.xixi.mall.rbac.entity;


import com.xixi.mall.common.core.webbase.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色与菜单对应关系
 */
@Getter
@Setter
public class RoleMenuEntity extends BaseEntity {

    /**
     * 关联id
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单资源用户id
     */
    private Long menuPermissionId;

}
