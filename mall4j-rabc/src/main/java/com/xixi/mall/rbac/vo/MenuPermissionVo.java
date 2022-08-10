package com.xixi.mall.rbac.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单资源VO
 */
@Setter
@Getter
@ToString
public class MenuPermissionVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单资源用户id")
    private Long menuPermissionId;

    @ApiModelProperty("资源关联菜单")
    private Long menuId;

	@ApiModelProperty("菜单标题")
	private String menuTitle;

    @ApiModelProperty("权限对应的编码")
    private String permission;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源对应服务器路径")
    private String uri;

    @ApiModelProperty("请求方法 1.GET 2.POST 3.PUT 4.DELETE")
    private Integer method;

}
