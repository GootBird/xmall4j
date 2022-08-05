package com.xixi.mall.auth.feign;

import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.api.auth.feign.TokenFeignClient;
import com.xixi.mall.auth.manager.TokenStore;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class TokenFeignController implements TokenFeignClient {


    @Resource
    private TokenStore tokenStore;

    @Override
    public ServerResponse<UserInfoInTokenBo> checkToken(String accessToken) {
        UserInfoInTokenBo userInfoByAccessTokenResponse = tokenStore.getUserInfoByAccessToken(accessToken, true);
        return ServerResponse.success(userInfoByAccessTokenResponse);
    }

}
