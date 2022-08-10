package com.xixi.mall.rbac.vo;

import com.xixi.mall.common.core.webbase.vo.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class RoleVo extends BaseVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建者ID")
    private Long createUserId;

    @ApiModelProperty("所属租户id")
    private Long tenantId;

    @ApiModelProperty("类型")
    private Integer bizType;

    @ApiModelProperty("菜单id列表")
    private List<Long> menuIds;

    @ApiModelProperty("菜单资源id列表")
    private List<Long> menuPermissionIds;

}
