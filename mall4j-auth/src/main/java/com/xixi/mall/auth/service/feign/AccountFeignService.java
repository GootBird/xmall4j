package com.xixi.mall.auth.service.feign;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.api.auth.constant.SysTypeEnum;
import com.xixi.mall.api.auth.dto.AuthAccountDto;
import com.xixi.mall.api.auth.vo.AuthAccountVo;
import com.xixi.mall.api.auth.vo.TokenInfoVo;
import com.xixi.mall.auth.entity.AuthAccount;
import com.xixi.mall.auth.manager.TokenStore;
import com.xixi.mall.auth.mapper.AuthAccountMapper;
import com.xixi.mall.common.core.enums.ResponseEnum;
import com.xixi.mall.common.core.utils.PrincipalUtil;
import com.xixi.mall.common.core.utils.ThrowUtils;
import com.xixi.mall.common.security.bo.AuthAccountInVerifyBo;
import com.xixi.mall.common.security.constant.InputUserNameEnum;
import com.xixi.mall.common.security.context.AuthUserContext;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static com.xixi.mall.common.core.constant.Constant.VOID;

@Service
public class AccountFeignService {


    @Resource
    private AuthAccountMapper authAccountMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private TokenStore tokenStore;

    private final SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Transactional(rollbackFor = Exception.class)
    public Long save(AuthAccountDto authAccountDto) {

        AuthAccount newAccount = verify(authAccountDto);
        newAccount.setUid(snowflakeGenerator.next());
        authAccountMapper.save(newAccount);

        return newAccount.getUid();
    }

    @Transactional(rollbackFor = Exception.class)
    public Void update(AuthAccountDto authAccountDto) {
        AuthAccount verify = verify(authAccountDto);
        authAccountMapper.updateAccountInfo(verify);

        return VOID;
    }

    @Transactional(rollbackFor = Exception.class)
    public Void updateAccountStatus(AuthAccountDto authAccountDto) {

        Optional.ofNullable(authAccountDto.getStatus())
                .orElseThrow(ThrowUtils.getSupErr(ResponseEnum.EXCEPTION));

        AuthAccount authAccount = mapperFacade.map(authAccountDto, AuthAccount.class);
        authAccountMapper.updateAccountInfo(authAccount);

        return VOID;
    }


    @Transactional(rollbackFor = Exception.class)
    public Void deleteById(Long userId) {
        UserInfoInTokenBo userInfoInTokenBo = AuthUserContext.get();
        authAccountMapper.deleteByUserIdAndSysType(userId, userInfoInTokenBo.getSysType());

        return VOID;
    }

    public AuthAccountVo getById(Long userId) {
        UserInfoInTokenBo userInfoInTokenBo = AuthUserContext.get();
        AuthAccount authAccount = authAccountMapper.getByUserIdAndType(userId, userInfoInTokenBo.getSysType());
        return mapperFacade.map(authAccount, AuthAccountVo.class);
    }

    public TokenInfoVo storeTokenAndGet(UserInfoInTokenBo userInfoInTokenBo) {
        return tokenStore.storeAndGetVo(userInfoInTokenBo);
    }


    public AuthAccountVo getByUsername(String username, SysTypeEnum sysType) {
        return authAccountMapper.getByUsernameAndSysType(username, sysType.value());
    }

    private AuthAccount verify(AuthAccountDto authAccountDTO) {
        // 用户名
        if (!PrincipalUtil.isUserName(authAccountDTO.getUsername())) {
            ThrowUtils.throwErr("用户名格式不正确");
        }

        AuthAccountInVerifyBo userNameBo = authAccountMapper.getAuthAccountInVerifyByInputUserName(
                InputUserNameEnum.USERNAME.getValue(),
                authAccountDTO.getUsername(),
                authAccountDTO.getSysType()
        );

        if (userNameBo != null && !Objects.equals(userNameBo.getUserId(), authAccountDTO.getUserId())) {
            ThrowUtils.throwErr("用户名已存在，请更换用户名再次尝试");
        }

        AuthAccount authAccount = mapperFacade.map(authAccountDTO, AuthAccount.class);

        if (StrUtil.isNotBlank(authAccount.getPassword())) {
            authAccount.setPassword(passwordEncoder.encode(authAccount.getPassword()));
        }

        return authAccount;
    }

    @Transactional(rollbackFor = Exception.class)
    public Void updateUser(UserInfoInTokenBo userInfoInTokenBo, Long userId, Integer sysType) {
        AuthAccount byUserIdAndType = authAccountMapper.getByUserIdAndType(userId, sysType);
        userInfoInTokenBo.setUid(byUserIdAndType.getUid());
        tokenStore.updateUserInfoByUidAndAppId(byUserIdAndType.getUid(), sysType.toString(), userInfoInTokenBo);
        AuthAccount authAccount = mapperFacade.map(userInfoInTokenBo, AuthAccount.class);
        int res = authAccountMapper.updateUserInfoByUserId(authAccount, userId, sysType);
        if (res != 1) {
            ThrowUtils.throwErr("用户信息错误，更新失败");
        }
        return VOID;
    }

    public AuthAccountVo getMerchantByTenantId(Long tenantId) {
        return authAccountMapper.getMerchantInfoByTenantId(tenantId);
    }
}
