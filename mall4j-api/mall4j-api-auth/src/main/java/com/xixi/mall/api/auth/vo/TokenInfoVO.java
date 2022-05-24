package com.xixi.mall.api.auth.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * token信息，该信息用户返回给前端，前端请求携带accessToken进行用户校验
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class TokenInfoVO {

    @ApiModelProperty("accessToken")
    private String accessToken;

    @ApiModelProperty("refreshToken")
    private String refreshToken;

    @ApiModelProperty("在多少秒后过期")
    private Integer expiresIn;

}
