package com.xixi.mall.api.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统类型
 */
@Getter
@AllArgsConstructor
public enum SysTypeEnum {

    /**
     * 普通用户系统
     */
    ORDINARY(0),

    /**
     * 商家端
     */
    MULTISHOP(1),

    /**
     * 平台端
     */
    PLATFORM(2),

    ;

    private final Integer value;

}
