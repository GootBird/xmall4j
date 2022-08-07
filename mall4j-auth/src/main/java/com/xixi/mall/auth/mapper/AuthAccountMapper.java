package com.xixi.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xixi.mall.api.auth.vo.AuthAccountVo;
import com.xixi.mall.auth.entity.AuthAccountEntity;
import com.xixi.mall.common.security.bo.AuthAccountInVerifyBo;
import org.apache.ibatis.annotations.Param;

public interface AuthAccountMapper extends BaseMapper<AuthAccountEntity> {

    /**
     * 根据输入的用户名及用户名类型获取用户信息
     *
     * @param inputUserNameType 输入的用户名类型 1.username 2.mobile 3.email
     * @param inputUserName     输入的用户名
     * @param sysType           系统类型
     * @return 用户在token中信息 + 数据库中的密码
     */
    AuthAccountInVerifyBo getAuthAccountInVerifyByInputUserName(@Param("inputUserNameType") Integer inputUserNameType,
                                                                @Param("inputUserName") String inputUserName,
                                                                @Param("sysType") Integer sysType);

    /**
     * 根据用户id 和系统类型获取平台唯一用户
     *
     * @param userId  用户id
     * @param sysType 系统类型
     * @return 平台唯一用户
     */
    AuthAccountEntity getByUserIdAndType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

    /**
     * 根据getByUid获取平台唯一用户
     *
     * @param uid uid
     * @return 平台唯一用户
     */
    AuthAccountEntity getByUid(@Param("uid") Long uid);

    /**
     * 更新密码 根据用户id 和系统类型
     *
     * @param userId      用户id
     * @param sysType     系统类型
     * @param newPassWord 新密码
     */
    void updatePassword(@Param("userId") Long userId, @Param("sysType") Integer sysType, @Param("newPassWord") String newPassWord);

    /**
     * 保存
     *
     * @param authAccountEntity 用户实体
     */
    void save(@Param("authAccount") AuthAccountEntity authAccountEntity);

    /**
     * 更新
     *
     * @param authAccountEntity authAccount
     */
    void updateAccountInfo(@Param("authAccount") AuthAccountEntity authAccountEntity);

    /**
     * 根据用户id和系统类型删除用户
     *
     * @param userId  用户id
     * @param sysType 系统类型
     */
    void deleteByUserIdAndSysType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

    /**
     * 根据用户名和系统类型获取用户信息
     *
     * @param validAccount
     * @param systemType
     * @return uid
     */
    AuthAccountEntity getAccountByInputUserName(@Param("validAccount") String validAccount, @Param("systemType") Integer systemType);

    /**
     * 根据用户名和系统类型获取用户信息
     *
     * @param username 用户名
     * @param sysType  系统类型
     * @return 用户
     */
    AuthAccountVo getByUsernameAndSysType(@Param("userName") String username,
                                          @Param("sysType") Integer sysType);

    /**
     * 根据用户id更新租户id
     *
     * @param authAccountEntity user
     * @param userId      用户ID
     * @param sysType     系统类型
     * @return res
     */
    int updateUserInfoByUserId(@Param("authAccount") AuthAccountEntity authAccountEntity,
                               @Param("userId") Long userId,
                               @Param("sysType") Integer sysType);

    /**
     * 根据租户id获取商家信息
     *
     * @param tenantId 租户Id
     * @return vo
     */
    AuthAccountVo getMerchantInfoByTenantId(@Param("tenantId") Long tenantId);
}
