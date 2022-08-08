package com.xixi.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xixi.mall.api.auth.vo.AuthAccountVo;
import com.xixi.mall.auth.entity.AuthAccountEntity;
import com.xixi.mall.common.security.bo.AuthAccountInVerifyBo;
import org.apache.ibatis.annotations.Param;

public interface AuthAccountMapper extends BaseMapper<AuthAccountEntity> {
}
