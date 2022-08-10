package com.xixi.mall.rbac.entity;

import com.xixi.mall.common.core.webbase.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色
 */
@Setter
@Getter
@ToString
public class RoleEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 业务类型 0平台菜单 1 店铺菜单
     */
    private Integer bizType;

    /**
     * 所属租户id
     */
    private Long tenantId;

}
