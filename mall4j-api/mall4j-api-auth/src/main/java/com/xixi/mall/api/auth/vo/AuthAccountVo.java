package com.xixi.mall.api.auth.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthAccountVo {
    /**
     * 全平台用户唯一id
     */
    private Long uid;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("状态 1:启用 0:禁用 -1:删除")
    private Integer status;

    @ApiModelProperty("创建ip")
    private String createIp;

}
