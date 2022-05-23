package com.xixi.mall.common.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.xixi.mall.**.mapper"})
public class MybatisPlusConfig {

}
