package com.xixi.mall.rbac;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "com.xixi.mall" })
@EnableFeignClients(basePackages = {"com.xixi.mall.api.**.feign"})
public class RbacApplication {
}
