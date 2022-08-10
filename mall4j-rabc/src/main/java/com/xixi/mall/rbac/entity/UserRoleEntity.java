package com.xixi.mall.rbac.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xixi.mall.common.core.webbase.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户与角色对应关系
 */
@Setter
@Getter
@ToString
@TableName("user_role")
public class UserRoleEntity extends BaseEntity {

    /**
     * 关联id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
