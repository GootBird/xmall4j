package com.xixi.mall.auth.service.sys;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xixi.mall.api.auth.bo.UserInfoInTokenBo;
import com.xixi.mall.api.auth.constant.SysTypeEnum;
import com.xixi.mall.api.auth.vo.TokenInfoVo;
import com.xixi.mall.common.cache.constant.CacheNames;
import com.xixi.mall.common.core.enums.ResponseEnum;
import com.xixi.mall.common.core.utils.PrincipalUtil;
import com.xixi.mall.common.core.utils.ThrowUtils;
import com.xixi.mall.common.security.bo.TokenInfoBo;
import io.seata.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * token管理 1. 登陆返回token 2. 刷新token 3. 清除用户过去token 4. 校验token
 */
@Component
@RefreshScope
public class TokenStoreSysService {

    private static final Logger logger = LoggerFactory.getLogger(TokenStoreSysService.class);

    private final RedisTemplate<Object, Object> redisTemplate;

    private final RedisSerializer<Object> redisSerializer;

    private final StringRedisTemplate stringRedisTemplate;

    public TokenStoreSysService(RedisTemplate<Object, Object> redisTemplate,
                                RedisSerializer<Object> redisSerializer,
                                StringRedisTemplate stringRedisTemplate) {

        this.redisTemplate = redisTemplate;
        this.redisSerializer = redisSerializer;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 将用户的部分信息存储在token中，并返回token信息
     *
     * @param userInfoInToken 用户在token中的信息
     * @return token信息
     */
    public TokenInfoBo storeAccessToken(UserInfoInTokenBo userInfoInToken) {

        TokenInfoBo tokenInfoBo = new TokenInfoBo();

        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();

        tokenInfoBo.setUserInfoInToken(userInfoInToken);
        tokenInfoBo.setExpiresIn(getExpiresIn(userInfoInToken.getSysType()));

        String uidToAccessKeyStr = getUidToAccessKey(getApprovalKey(userInfoInToken));
        String accessKeyStr = getAccessKey(accessToken);
        String refreshToAccessKeyStr = getRefreshToAccessKey(refreshToken);

        // 一个用户会登陆很多次，每次登陆的token都会存在 uid_to_access里面
        // 但是每次保存都会更新这个key的时间，而key里面的token有可能会过期，过期就要移除掉
        List<String> existsAccessTokens = new LinkedList<>();
        // 新的token数据
        existsAccessTokens.add(accessToken + StrUtil.COLON + refreshToken);

        Optional.ofNullable(redisTemplate.opsForSet().size(uidToAccessKeyStr))
                .filter(size -> size != 0)
                .map(size -> stringRedisTemplate.opsForSet().pop(uidToAccessKeyStr, size))
                .filter(CollectionUtils::isNotEmpty)
                .ifPresent(tokenInfoBoList -> tokenInfoBoList.forEach(
                        accessTokenWithRefreshToken -> {
                            String accessTokenData = accessTokenWithRefreshToken.split(StrUtil.COLON)[0];
                            if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(getAccessKey(accessTokenData)))) {
                                existsAccessTokens.add(accessTokenWithRefreshToken);
                            }
                        })
                );

        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {

            long expiresIn = tokenInfoBo.getExpiresIn();

            byte[] uidKey = uidToAccessKeyStr.getBytes(StandardCharsets.UTF_8),
                    refreshKey = refreshToAccessKeyStr.getBytes(StandardCharsets.UTF_8),
                    accessKey = accessKeyStr.getBytes(StandardCharsets.UTF_8);

            for (String existsAccessToken : existsAccessTokens) {
                connection.sAdd(uidKey, existsAccessToken.getBytes(StandardCharsets.UTF_8));
            }

            // 通过uid + sysType 保存access_token，当需要禁用用户的时候，可以根据uid + sysType 禁用用户
            connection.expire(uidKey, expiresIn);

            // 通过refresh_token获取用户的access_token从而刷新token
            connection.setEx(refreshKey, expiresIn, accessToken.getBytes(StandardCharsets.UTF_8));

            // 通过access_token保存用户的租户id，用户id，uid
            connection.setEx(accessKey, expiresIn, Objects.requireNonNull(redisSerializer.serialize(userInfoInToken)));

            return null;
        });

        // 返回给前端是加密的token
        tokenInfoBo.setAccessToken(encryptToken(accessToken, userInfoInToken.getSysType()));
        tokenInfoBo.setRefreshToken(encryptToken(refreshToken, userInfoInToken.getSysType()));

        return tokenInfoBo;
    }

    private int getExpiresIn(int sysType) {
        // 3600秒
        int expiresIn = 3600;

        // 普通用户token过期时间 1小时
        if (Objects.equals(sysType, SysTypeEnum.ORDINARY.getValue())) {
            expiresIn = expiresIn * 24 * 30;
        }
        // 系统管理员的token过期时间 2小时
        if (Objects.equals(sysType, SysTypeEnum.MULTISHOP.getValue())
                || Objects.equals(sysType, SysTypeEnum.PLATFORM.getValue())) {
            expiresIn = expiresIn * 24 * 30;
        }
        return expiresIn;
    }

    /**
     * 根据accessToken 获取用户信息
     *
     * @param accessToken accessToken
     * @param needDecrypt 是否需要解密
     * @return 用户信息
     */
    public UserInfoInTokenBo getUserInfoByAccessToken(String accessToken, Boolean needDecrypt) {

        if (StrUtil.isBlank(accessToken)) {
            ThrowUtils.throwErr("accessToken is blank");
        }

        String realAccessToken = needDecrypt
                ? decryptToken(accessToken)
                : accessToken;

        UserInfoInTokenBo userInfoInTokenBo = (UserInfoInTokenBo) redisTemplate.opsForValue()
                .get(getAccessKey(realAccessToken));

        if (userInfoInTokenBo == null) {
            ThrowUtils.throwErr("accessToken 已过期");
        }

        return userInfoInTokenBo;
    }

    /**
     * 刷新token，并返回新的token
     *
     * @param refreshToken 被刷新的token
     * @return 新的token信息
     */
    public TokenInfoBo refreshToken(String refreshToken) {

        if (StrUtil.isBlank(refreshToken)) {
            ThrowUtils.throwErr("refreshToken is blank");
        }

        String realRefreshToken = decryptToken(refreshToken);

        String accessToken = stringRedisTemplate.opsForValue().get(getRefreshToAccessKey(realRefreshToken));

        if (StrUtil.isBlank(accessToken)) {
            ThrowUtils.throwErr("refreshToken 已过期");
        }

        UserInfoInTokenBo userInfoInTokenBo = getUserInfoByAccessToken(accessToken, false);

        // 删除旧的refresh_token
        stringRedisTemplate.delete(getRefreshToAccessKey(realRefreshToken));

        // 删除旧的access_token
        stringRedisTemplate.delete(getAccessKey(accessToken));

        // 保存一份新的token
        return storeAccessToken(userInfoInTokenBo);
    }

    /**
     * 删除全部的token
     */
    public void deleteAllToken(String appId, Long uid) {
        String uidKey = getUidToAccessKey(getApprovalKey(appId, uid));
        Long size = redisTemplate.opsForSet().size(uidKey);

        if (size == null || size == 0) return;

        List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidKey, size);

        if (CollUtil.isEmpty(tokenInfoBoList)) return;

        for (String accessTokenWithRefreshToken : tokenInfoBoList) {

            String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);

            String accessToken = accessTokenWithRefreshTokenArr[0];
            String refreshToken = accessTokenWithRefreshTokenArr[1];

            redisTemplate.delete(getRefreshToAccessKey(refreshToken));
            redisTemplate.delete(getAccessKey(accessToken));
        }

        redisTemplate.delete(uidKey);
    }

    private static String getApprovalKey(UserInfoInTokenBo userInfoInToken) {
        return getApprovalKey(userInfoInToken.getSysType().toString(), userInfoInToken.getUid());
    }

    private static String getApprovalKey(String appId, Long uid) {
        return uid == null ? appId : appId + StrUtil.COLON + uid;
    }

    private String encryptToken(String accessToken, Integer sysType) {
        return Base64.encode(accessToken + System.currentTimeMillis() + sysType);
    }

    private String decryptToken(String data) {
        String decryptStr;
        String decryptToken = null;
        try {
            decryptStr = Base64.decodeStr(data);
            decryptToken = decryptStr.substring(0, 32);

            // 创建token的时间，token使用时效性，防止攻击者通过一堆的尝试找到aes的密码，虽然aes是目前几乎最好的加密算法
            long createTokenTime = Long.parseLong(decryptStr.substring(32, 45));

            // 系统类型
            int sysType = Integer.parseInt(decryptStr.substring(45));

            // token的过期时间
            int expiresIn = getExpiresIn(sysType);

            long second = 1000L;
            if (System.currentTimeMillis() - createTokenTime > expiresIn * second) {
                ThrowUtils.throwErr("token 格式有误");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            ThrowUtils.throwErr("token 格式有误");
        }

        // 防止解密后的token是脚本，从而对redis进行攻击，uuid只能是数字和小写字母
        if (!PrincipalUtil.isSimpleChar(decryptToken)) {
            ThrowUtils.throwErr("token 格式有误");
        }

        return decryptToken;
    }

    public String getAccessKey(String accessToken) {
        return CacheNames.ACCESS + accessToken;
    }

    public String getUidToAccessKey(String approvalKey) {
        return CacheNames.UID_TO_ACCESS + approvalKey;
    }

    public String getRefreshToAccessKey(String refreshToken) {
        return CacheNames.REFRESH_TO_ACCESS + refreshToken;
    }

    public TokenInfoVo storeAndGetVo(UserInfoInTokenBo userInfoInToken) {

        TokenInfoBo tokenInfoBo = storeAccessToken(userInfoInToken);

        return new TokenInfoVo()
                .setAccessToken(tokenInfoBo.getAccessToken())
                .setRefreshToken(tokenInfoBo.getRefreshToken())
                .setExpiresIn(tokenInfoBo.getExpiresIn());
    }

    public void updateUserInfoByUidAndAppId(Long uid, String appId, UserInfoInTokenBo userInfoInTokenBo) {
        if (userInfoInTokenBo == null) {
            return;
        }
        String uidKey = getUidToAccessKey(getApprovalKey(appId, uid));
        Set<String> tokenInfoBoList = stringRedisTemplate.opsForSet().members(uidKey);
        if (tokenInfoBoList == null || tokenInfoBoList.size() == 0) {
            ThrowUtils.throwErr(ResponseEnum.UNAUTHORIZED);
        }
        for (String accessTokenWithRefreshToken : tokenInfoBoList) {
            String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
            String accessKey = this.getAccessKey(accessTokenWithRefreshTokenArr[0]);
            UserInfoInTokenBo oldUserInfoInTokenBo = (UserInfoInTokenBo) redisTemplate.opsForValue().get(accessKey);
            if (oldUserInfoInTokenBo == null) {
                continue;
            }
            BeanUtils.copyProperties(userInfoInTokenBo, oldUserInfoInTokenBo);
            redisTemplate.opsForValue().set(accessKey, Objects.requireNonNull(userInfoInTokenBo), getExpiresIn(userInfoInTokenBo.getSysType()), TimeUnit.SECONDS);
        }
    }
}
