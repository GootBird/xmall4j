package com.xixi.mall.api.rabc.feign;

import com.xixi.mall.api.rabc.dto.ClearUserPermissionsCacheDto;
import com.xixi.mall.common.core.feign.FeignInsideAuthConfig;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@RequestMapping(FeignInsideAuthConfig.FEIGN_URL + "/permission")
@FeignClient(value = PermissionFeignClient.SERVICE_NAME, contextId = "permission")
public interface PermissionFeignClient {

    String SERVICE_NAME = "mall4cloud-rbac";

    /**
     * 校验是否有某个uri的权限
     *
     * @param userId  用户ID
     * @param sysType 系统类型
     * @param uri     uri
     * @param isAdmin 是否管理员
     * @param method  method
     * @return 是否有某个uri的权限
     */
    @GetMapping(FeignInsideAuthConfig.INSIDER_URL + "/checkPermission")
    ServerResponse<Boolean> checkPermission(@RequestParam("userId") Long userId,
                                            @RequestParam("sysType") Integer sysType,
                                            @RequestParam("uri") String uri,
                                            @RequestParam("isAdmin") Integer isAdmin,
                                            @RequestParam("method") Integer method);

    /**
     * 清除用户权限缓存
     *
     * @param clearUserPermissionsCacheDto dto
     * @return 是否调用成功
     */
    @PostMapping(FeignInsideAuthConfig.INSIDER_URL + "/clearUserPermissionsCache")
    ServerResponse<Void> clearUserPermissionsCache(@RequestBody ClearUserPermissionsCacheDto clearUserPermissionsCacheDto);

}
