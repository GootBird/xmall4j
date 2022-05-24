package com.xixi.mall.api.auth.feign;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.constant.Auth;
import com.mall4j.cloud.common.response.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall4cloud-auth", contextId = "token")
public interface TokenFeignClient {

    /**
     * 校验token并返回token保存的用户信息
     *
     * @param accessToken accessToken
     * @return token保存的用户信息
     */
    @GetMapping(value = Auth.CHECK_TOKEN_URI)
    ServerResponse<UserInfoInTokenBO> checkToken(@RequestParam("accessToken") String accessToken);

}
