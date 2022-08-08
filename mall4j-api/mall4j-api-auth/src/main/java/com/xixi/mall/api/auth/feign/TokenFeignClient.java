package com.xixi.mall.api.auth.feign;

import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.common.core.feign.FeignInsideAuthConfig;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall4j-auth", contextId = "token")
@RequestMapping(FeignInsideAuthConfig.FEIGN_URL + "/token")
public interface TokenFeignClient {

    /**
     * 校验token并返回token保存的用户信息
     *
     * @param accessToken accessToken
     * @return token保存的用户信息
     */
    @GetMapping("/checkToken")
    ServerResponse<UserInfoInTokenBo> checkToken(@RequestParam("accessToken") String accessToken);

}
