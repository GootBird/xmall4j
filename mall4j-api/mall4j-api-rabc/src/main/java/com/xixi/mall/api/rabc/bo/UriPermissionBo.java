package com.xixi.mall.api.rabc.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * uri权限bo
 */
@Getter
@Setter
@ToString
public class UriPermissionBo {

    /**
     * 请求方法 1.GET 2.POST 3.PUT 4.DELETE
     */
    private Integer method;

    /**
     * uri
     */
    private String uri;

    /**
     * permission
     */
    private String permission;

}
