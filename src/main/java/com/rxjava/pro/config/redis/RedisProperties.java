package com.rxjava.pro.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "rx.redis")
public class RedisProperties {

	private String redisHost;
	private int redisPort;
}
