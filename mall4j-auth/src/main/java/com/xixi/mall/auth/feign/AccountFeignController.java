package com.xixi.mall.auth.feign;

import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.api.auth.constant.SysTypeEnum;
import com.xixi.mall.api.auth.dto.AuthAccountDto;
import com.xixi.mall.api.auth.feign.AccountFeignClient;
import com.xixi.mall.api.auth.vo.AuthAccountVo;
import com.xixi.mall.api.auth.vo.TokenInfoVo;
import com.xixi.mall.auth.service.feign.AccountFeignService;
import com.xixi.mall.common.core.aop.PackResponseEnhance;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AccountFeignController implements AccountFeignClient {

    @Resource
    private AccountFeignService service;

    @Override
    public ServerResponse<Long> save(AuthAccountDto authAccountDto) {
        return PackResponseEnhance.enhance(() -> service.save(authAccountDto));
    }

    @Override
    public ServerResponse<Void> update(AuthAccountDto authAccountDTO) {
        return PackResponseEnhance.enhance(() -> service.update(authAccountDTO));
    }

    @Override
    public ServerResponse<Void> updateAccountStatus(AuthAccountDto authAccountDTO) {
        return PackResponseEnhance.enhance(() -> service.updateAccountStatus(authAccountDTO));
    }

    @Override
    public ServerResponse<Void> deleteById(Long userId) {
        return PackResponseEnhance.enhance(() -> service.deleteById(userId));
    }

    @Override
    public ServerResponse<AuthAccountVo> getById(Long userId) {
        return PackResponseEnhance.enhance(() -> service.getById(userId));

    }

    @Override
    public ServerResponse<TokenInfoVo> storeTokenAndGet(UserInfoInTokenBo userInfoInTokenBO) {
        return PackResponseEnhance.enhance(() -> service.storeTokenAndGet(userInfoInTokenBO));
    }

    @Override
    public ServerResponse<AuthAccountVo> getByUsername(String username, SysTypeEnum sysType) {
        return PackResponseEnhance.enhance(() -> service.getByUsername(username, sysType));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<Void> updateUser(UserInfoInTokenBo userInfoInTokenBo, Long userId, Integer sysType) {
        return PackResponseEnhance.enhance(
                () -> service.updateUser(userInfoInTokenBo, userId, sysType)
        );
    }

    @Override
    public ServerResponse<AuthAccountVo> getMerchantByTenantId(Long tenantId) {
        return PackResponseEnhance.enhance(
                () -> service.getMerchantByTenantId(tenantId)
        );
    }

}
