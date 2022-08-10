package com.xixi.mall.rbac.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单资源简易VO
 */
@Setter
@Getter
@ToString
public class MenuPermissionSimpleVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单资源用户id")
    private Long menuPermissionId;

    @ApiModelProperty("资源关联菜单")
    private Long menuId;

    @ApiModelProperty("资源名称")
    private String name;

}
