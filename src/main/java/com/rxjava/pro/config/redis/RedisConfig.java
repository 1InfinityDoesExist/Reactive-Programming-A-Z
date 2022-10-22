package com.rxjava.pro.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "rx.redis", name = "enable", havingValue = "true")
public class RedisConfig {

	@Autowired
	private RedisProperties redisProperties;

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {

		log.info("-----Configuring Redis----");
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
				redisProperties.getRedisHost(), redisProperties.getRedisPort());
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}
}
