package com.xixi.mall.api.auth.feign;

import com.xixi.mall.api.auth.constant.SysTypeEnum;
import com.xixi.mall.api.auth.dto.AuthAccountDTO;
import com.xixi.mall.api.auth.vo.AuthAccountVO;
import com.xixi.mall.api.auth.vo.TokenInfoVO;
import com.xixi.mall.common.core.feign.FeignInsideAuthConfig;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "mall4cloud-auth", contextId = "account")
public interface AccountFeignClient {

    /**
     * 保存统一账户
     *
     * @param authAccountDTO 账户信息
     * @return Long uid
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account")
    ServerResponse<Long> save(@RequestBody AuthAccountDTO authAccountDTO);

    /**
     * 更新统一账户
     *
     * @param authAccountDTO 账户信息
     * @return void
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account")
    ServerResponse<Void> update(@RequestBody AuthAccountDTO authAccountDTO);

    /**
     * 更新账户状态
     *
     * @param authAccountDTO 账户信息
     * @return void
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account/status")
    ServerResponse<Void> updateAuthAccountStatus(@RequestBody AuthAccountDTO authAccountDTO);

    /**
     * 根据用户id和系统类型删除用户
     *
     * @param userId 用户id
     * @return void
     */
    @DeleteMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account/deleteByUserIdAndSysType")
    ServerResponse<Void> deleteByUserIdAndSysType(@RequestParam("userId") Long userId);

    /**
     * 根据用户id和系统类型获取用户信息
     *
     * @param userId  用户id
     * @param sysType 系统类型
     * @return void
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/account/getByUserIdAndSysType")
    ServerResponse<AuthAccountVO> getByUserIdAndSysType(@RequestParam("userId") Long userId, @RequestParam("sysType") Integer sysType);

    /**
     * 保存用户信息，生成token，返回前端
     *
     * @param userInfoInTokenBO 账户信息 和社交账号信息
     * @return uid
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/storeTokenAndGetVo")
    ServerResponse<TokenInfoVO> storeTokenAndGetVo(@RequestBody UserInfoInTokenBO userInfoInTokenBO);

    /**
     * 根据用户名和系统类型获取用户信息
     *
     * @param username
     * @param sysType
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getByUsernameAndSysType")
    ServerResponse<AuthAccountVO> getByUsernameAndSysType(@RequestParam("userName") String username
            , @RequestParam("sysType") SysTypeEnum sysType);

    /**
     * 根据用户id与用户类型更新用户信息
     *
     * @param userInfoInTokenBO 新的用户信息
     * @param userId            用户id
     * @param sysType           用户类型
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/accout/updateTenantIdByUserIdAndSysType")
    ServerResponse<Void> updateUserInfoByUserIdAndSysType(@RequestBody UserInfoInTokenBO userInfoInTokenBO
            , @RequestParam("userId") Long userId
            , @RequestParam("sysType") Integer sysType);

    /**
     * 根据租户id查询商家信息
     *
     * @param tenantId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account/getMerchantInfoByTenantId")
    ServerResponse<AuthAccountVO> getMerchantInfoByTenantId(@RequestParam("tenantId") Long tenantId);

}
