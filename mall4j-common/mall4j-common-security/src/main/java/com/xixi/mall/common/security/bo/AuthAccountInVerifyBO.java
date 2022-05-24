package com.xixi.mall.common.security.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于校验的用户信息
 */
@Getter
@Setter
public class AuthAccountInVerifyBO extends UserInfoInTokenBO {

	private String password;

	private Integer status;

}
