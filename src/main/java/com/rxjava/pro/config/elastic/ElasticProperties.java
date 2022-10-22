package com.rxjava.pro.config.elastic;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "rx.elastic")
public class ElasticProperties implements Serializable {

	private String elasticHost;
	private int elasticPort;
}
