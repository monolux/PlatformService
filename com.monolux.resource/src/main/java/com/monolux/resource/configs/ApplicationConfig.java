package com.monolux.resource.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = { "com.monolux.domain" })
@EntityScan(basePackages = { "com.monolux.domain" })
@ComponentScan(basePackages = { "com.monolux.encryption" })
public class ApplicationConfig {

}