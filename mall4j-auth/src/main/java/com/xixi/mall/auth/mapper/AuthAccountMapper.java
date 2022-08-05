package com.xixi.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xixi.mall.api.auth.vo.AuthAccountVo;
import com.xixi.mall.auth.entity.AuthAccount;
import com.xixi.mall.common.security.bo.AuthAccountInVerifyBo;
import org.apache.ibatis.annotations.Param;

public interface AuthAccountMapper extends BaseMapper<AuthAccount> {

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
    AuthAccount getByUserIdAndType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

    /**
     * 根据getByUid获取平台唯一用户
     *
     * @param uid uid
     * @return 平台唯一用户
     */
    AuthAccount getByUid(@Param("uid") Long uid);

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
     * @param authAccount 用户实体
     */
    void save(@Param("authAccount") AuthAccount authAccount);

    /**
     * 更新
     *
     * @param authAccount authAccount
     */
    void updateAccountInfo(@Param("authAccount") AuthAccount authAccount);

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
    AuthAccount getAccountByInputUserName(@Param("validAccount") String validAccount, @Param("systemType") Integer systemType);

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
     * @param authAccount
     * @param userId
     * @param sysType
     * @return
     */
    int updateUserInfoByUserId(@Param("authAccount") AuthAccount authAccount,
                               @Param("userId") Long userId,
                               @Param("sysType") Integer sysType);

    /**
     * 根据租户id获取商家信息
     *
     * @param tenantId
     * @return
     */
    AuthAccountVo getMerchantInfoByTenantId(@Param("tenantId") Long tenantId);
}
