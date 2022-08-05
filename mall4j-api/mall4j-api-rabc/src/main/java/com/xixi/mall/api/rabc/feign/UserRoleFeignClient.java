package com.xixi.mall.api.rabc.feign;

import com.xixi.mall.api.rabc.dto.UserRoleDto;
import com.xixi.mall.common.core.feign.FeignInsideAuthConfig;
import com.xixi.mall.common.core.webbase.vo.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "mall4cloud-rbac", contextId = "userRole")
@RequestMapping(FeignInsideAuthConfig.FEIGN_URL + "/userRole")
public interface UserRoleFeignClient {

    /**
     * 保存用户角色关联信息
     *
     * @param userRoleDTO 用户角色关联信息
     * @return void
     */
    @PostMapping("/saveByUserIdAndSysType")
    ServerResponse<Void> saveByUserIdAndSysType(@RequestBody UserRoleDto userRoleDTO);

    /**
     * 更新用户角色关联信息
     *
     * @param userRoleDTO 用户角色关联信息
     * @return void
     */
    @PutMapping("/updateByUserIdAndSysType")
    ServerResponse<Void> updateByUserIdAndSysType(@RequestBody UserRoleDto userRoleDTO);

    /**
     * 删除用户角色关联信息
     *
     * @param userId 用户id
     * @return void
     */
    @DeleteMapping("/deleteByUserIdAndSysType")
    ServerResponse<Void> deleteByUserIdAndSysType(@RequestParam("userId") Long userId);

    /**
     * 获取用户角色关联信息
     *
     * @param userId 用户id
     * @return 用户角色关联ids
     */
    @GetMapping("/getRoleIds")
    ServerResponse<List<Long>> getRoleIds(@RequestParam("userId") Long userId);

}
