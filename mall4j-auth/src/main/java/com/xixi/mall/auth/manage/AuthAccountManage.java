package com.xixi.mall.auth.manage;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xixi.mall.auth.entity.AuthAccountEntity;
import com.xixi.mall.auth.mapper.AuthAccountMapper;
import com.xixi.mall.common.security.bo.AuthAccountInVerifyBo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class AuthAccountManage {

    @Resource
    private AuthAccountMapper authAccountMapper;

    /**
     * 根据输入的用户名及用户名类型获取用户信息
     *
     * @param inputUserNameType 输入的用户名类型 1.username 2.mobile 3.email
     * @param inputUserName     输入的用户名
     * @param sysType           系统类型
     * @return 用户在token中信息 + 数据库中的密码
     */
    public AuthAccountInVerifyBo getAuthAccountInVerifyByInputUserName(Integer inputUserNameType,
                                                                       String inputUserName,
                                                                       Integer sysType) {
        AuthAccountEntity accountEntity = authAccountMapper.selectOne(
                Wrappers.<AuthAccountEntity>lambdaQuery()
                        .eq(AuthAccountEntity::getSysType, sysType)
                        .eq(Objects.equals(inputUserNameType, 1), AuthAccountEntity::getUsername, inputUserName)
                        .ne(AuthAccountEntity::getStatus, -1)
        );

        AuthAccountInVerifyBo verifyBo = new AuthAccountInVerifyBo();
        BeanUtils.copyProperties(accountEntity, verifyBo);

        return verifyBo;
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateAccountInfo(AuthAccountEntity authAccountEntity) {

        authAccountMapper.updateAccountInfo(authAccountEntity);

        String userName = authAccountEntity.getUsername(),
                password = authAccountEntity.getPassword();

        Integer status = authAccountEntity.getStatus();

        authAccountMapper.update(null,
                Wrappers.<AuthAccountEntity>lambdaUpdate()
                        .set(StringUtils.isNotBlank(userName), AuthAccountEntity::getUsername, userName)
                        .set(StringUtils.isNotBlank(password), AuthAccountEntity::getPassword, password)
                        .set(Objects.nonNull(status), AuthAccountEntity::getStatus, status)
                        .eq(AuthAccountEntity::getUserId, authAccountEntity.getUserId())
                        .eq(AuthAccountEntity::getSysType, authAccountEntity.getSysType())
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIdAndSysType(Long userId, Integer sysType) {

        authAccountMapper.update(null,
                Wrappers.<AuthAccountEntity>lambdaUpdate()
                        .set(AuthAccountEntity::getStatus, -1)
                        .eq(AuthAccountEntity::getUserId, userId)
                        .eq(AuthAccountEntity::getSysType, sysType)
        );
    }

    public AuthAccountEntity getByUserIdAndType(Long userId, Integer sysType) {

        return authAccountMapper.selectOne(
                Wrappers.<AuthAccountEntity>lambdaQuery()
                        .eq(AuthAccountEntity::getSysType, sysType)
                        .eq(AuthAccountEntity::getUserId, userId)
        );
    }

}
