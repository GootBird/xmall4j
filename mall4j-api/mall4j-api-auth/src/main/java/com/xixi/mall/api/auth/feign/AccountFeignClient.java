package com.xixi.mall.api.auth.feign;

import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.api.auth.constant.SysTypeEnum;
import com.xixi.mall.api.auth.dto.AuthAccountDto;
import com.xixi.mall.api.auth.vo.AuthAccountVo;
import com.xixi.mall.api.auth.vo.TokenInfoVo;
import com.xixi.mall.common.core.feign.FeignInsideAuthConfig;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "mall4j-auth", contextId = "account")
@RequestMapping(FeignInsideAuthConfig.FEIGN_URL + "/account")
public interface AccountFeignClient {

    /**
     * 保存统一账户
     *
     * @param authAccountDto 账户信息
     * @return Long uid
     */
    @PostMapping(FeignInsideAuthConfig.INSIDER_URL + "/save")
    ServerResponse<Long> save(@RequestBody AuthAccountDto authAccountDto);

    /**
     * 更新统一账户
     *
     * @param authAccountDto 账户信息
     * @return void
     */
    @PutMapping("/update")
    ServerResponse<Void> update(@RequestBody AuthAccountDto authAccountDto);

    /**
     * 更新账户状态
     *
     * @param authAccountDto 账户信息
     * @return void
     */
    @PutMapping("/updateAccountStatus")
    ServerResponse<Void> updateAccountStatus(@RequestBody AuthAccountDto authAccountDto);

    /**
     * 根据用户id和系统类型删除用户
     *
     * @param userId 用户id
     * @return void
     */
    @DeleteMapping("/deleteById")
    ServerResponse<Void> deleteById(@RequestParam("userId") Long userId);

    /**
     * 根据用户id和系统类型获取用户信息
     *
     * @param userId  用户id
     * @return void
     */
    @GetMapping("/getById")
    ServerResponse<AuthAccountVo> getById(@RequestParam("userId") Long userId);

    /**
     * 保存用户信息，生成token，返回前端
     *
     * @param userInfoInTokenBo 账户信息 和社交账号信息
     * @return uid
     */
    @PostMapping(FeignInsideAuthConfig.INSIDER_URL + "/storeTokenAndGet")
    ServerResponse<TokenInfoVo> storeTokenAndGet(@RequestBody UserInfoInTokenBo userInfoInTokenBo);

    /**
     * 根据用户名和系统类型获取用户信息
     *
     * @param username 用户名
     * @param sysType  系统类型
     * @return resp
     */
    @PostMapping(FeignInsideAuthConfig.INSIDER_URL + "/getByUsername")
    ServerResponse<AuthAccountVo> getByUsername(@RequestParam("userName") String username,
                                                @RequestParam("sysType") SysTypeEnum sysType);

    /**
     * 根据用户id与用户类型更新用户信息
     *
     * @param userInfoInTokenBo 新的用户信息
     * @param userId            用户id
     * @param sysType           用户类型
     * @return resp
     */
    @PutMapping(FeignInsideAuthConfig.INSIDER_URL + "updateUser")
    ServerResponse<Void> updateUser(@RequestBody UserInfoInTokenBo userInfoInTokenBo,
                                    @RequestParam("userId") Long userId,
                                    @RequestParam("sysType") Integer sysType);

    /**
     * 根据租户id查询商家信息
     *
     * @param tenantId 租户Id
     * @return resp
     */
    @GetMapping(FeignInsideAuthConfig.INSIDER_URL + "/getMerchantByTenantId")
    ServerResponse<AuthAccountVo> getMerchantByTenantId(@RequestParam("tenantId") Long tenantId);

}
