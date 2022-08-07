package com.xixi.mall.auth.service.feign;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.api.auth.constant.SysTypeEnum;
import com.xixi.mall.api.auth.dto.AuthAccountDto;
import com.xixi.mall.api.auth.vo.AuthAccountVo;
import com.xixi.mall.api.auth.vo.TokenInfoVo;
import com.xixi.mall.auth.entity.AuthAccountEntity;
import com.xixi.mall.auth.manage.AuthAccountManage;
import com.xixi.mall.auth.service.sys.TokenStoreSysService;
import com.xixi.mall.auth.mapper.AuthAccountMapper;
import com.xixi.mall.common.core.constant.StatusEnum;
import com.xixi.mall.common.core.enums.ResponseEnum;
import com.xixi.mall.common.core.utils.PrincipalUtil;
import com.xixi.mall.common.core.utils.ThrowUtils;
import com.xixi.mall.common.security.context.AuthUserContext;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

import static com.xixi.mall.common.core.constant.Constant.VOID;

@Service
public class AccountFeignService {


    @Resource
    private AuthAccountManage authAccountManage;

    @Resource
    private AuthAccountMapper authAccountMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private TokenStoreSysService tokenStoreSysService;

    private final SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Transactional(rollbackFor = Exception.class)
    public Long save(AuthAccountDto authAccountDto) {

        AuthAccountEntity newAccount = verifyUserIsExist(authAccountDto);
        newAccount.setUid(snowflakeGenerator.next());

        authAccountMapper.insert(newAccount);

        return newAccount.getUid();
    }

    public Void update(AuthAccountDto authAccountDto) {

        AuthAccountEntity newAccount = verifyUserIsExist(authAccountDto);
        authAccountManage.updateAccountInfo(newAccount);
        return VOID;
    }

    public Void updateAccountStatus(AuthAccountDto authAccountDto) {

        Optional.ofNullable(authAccountDto.getStatus())
                .orElseThrow(ThrowUtils.getSupErr(ResponseEnum.EXCEPTION));

        AuthAccountEntity authAccountEntity = mapperFacade.map(authAccountDto, AuthAccountEntity.class);
        authAccountManage.updateAccountInfo(authAccountEntity);

        return VOID;
    }


    public Void deleteById(Long userId) {

        UserInfoInTokenBo userInfoInTokenBo = AuthUserContext.get();

        authAccountManage.deleteByUserIdAndSysType(userId, userInfoInTokenBo.getSysType());
        return VOID;
    }

    public AuthAccountVo getById(Long userId) {
        UserInfoInTokenBo userInfoInTokenBo = AuthUserContext.get();
        AuthAccountEntity authAccountEntity = authAccountMapper.getByUserIdAndType(userId, userInfoInTokenBo.getSysType());

        return mapperFacade.map(authAccountEntity, AuthAccountVo.class);
    }

    public TokenInfoVo storeTokenAndGet(UserInfoInTokenBo userInfoInTokenBo) {
        return tokenStoreSysService.storeAndGetVo(userInfoInTokenBo);
    }


    public AuthAccountVo getByUsername(String username, SysTypeEnum sysType) {
        return authAccountMapper.getByUsernameAndSysType(username, sysType.getValue());
    }

    private AuthAccountEntity verifyUserIsExist(AuthAccountDto authAccountDto) {

        if (!PrincipalUtil.isUserName(authAccountDto.getUsername())) {
            ThrowUtils.throwErr("用户名格式不正确");
        }

        int count = authAccountMapper.selectCount(
                Wrappers.<AuthAccountEntity>lambdaQuery()
                        .eq(AuthAccountEntity::getSysType, SysTypeEnum.MULTISHOP.getValue())
                        .eq(AuthAccountEntity::getUsername, authAccountDto.getUsername())
                        .ne(AuthAccountEntity::getStatus, StatusEnum.DELETE.getValue())
                        .ne(AuthAccountEntity::getUserId, authAccountDto.getUserId())
        );

        if (count > 0) {
            ThrowUtils.throwErr("用户名已存在，请更换用户名再次尝试");
        }

        AuthAccountEntity authAccountEntity = mapperFacade.map(authAccountDto, AuthAccountEntity.class);

        if (StrUtil.isNotBlank(authAccountEntity.getPassword())) {
            authAccountEntity.setPassword(passwordEncoder.encode(authAccountEntity.getPassword()));
        }

        return authAccountEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public Void updateUser(UserInfoInTokenBo userInfoInTokenBo, Long userId, Integer sysType) {
        AuthAccountEntity byUserIdAndType = authAccountMapper.getByUserIdAndType(userId, sysType);
        userInfoInTokenBo.setUid(byUserIdAndType.getUid());
        tokenStoreSysService.updateUserInfoByUidAndAppId(byUserIdAndType.getUid(), sysType.toString(), userInfoInTokenBo);
        AuthAccountEntity authAccountEntity = mapperFacade.map(userInfoInTokenBo, AuthAccountEntity.class);
        int res = authAccountMapper.updateUserInfoByUserId(authAccountEntity, userId, sysType);
        if (res != 1) {
            ThrowUtils.throwErr("用户信息错误，更新失败");
        }
        return VOID;
    }

    public AuthAccountVo getMerchantByTenantId(Long tenantId) {
        return authAccountMapper.getMerchantInfoByTenantId(tenantId);
    }
}
