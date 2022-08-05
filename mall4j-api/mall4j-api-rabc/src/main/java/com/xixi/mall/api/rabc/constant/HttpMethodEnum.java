package com.xixi.mall.api.rabc.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 请求方法 1.GET 2.POST 3.PUT 4.DELETE http方法枚举
 *
 * @author FrozenWatermelon
 * @date 2020/9/3
 */
@Getter
@AllArgsConstructor
public enum HttpMethodEnum {

    /**
     * GET
     */
    GET(1, "GET"),

    /**
     * POST
     */
    POST(2, "POST"),

    /**
     * PUT
     */
    PUT(3, "PUT"),

    /**
     * DELETE
     */
    DELETE(4, "DELETE"),
    ;

    private final Integer value;

    private final String method;

    private final static Map<Integer, HttpMethodEnum> valueMap;

    static {
        valueMap = Stream.of(HttpMethodEnum.values())
                .collect(Collectors.toMap(HttpMethodEnum::getValue, Function.identity()));
    }

    public static HttpMethodEnum valueOf(Integer value) {
        return valueMap.get(value);
    }

}
