package com.rxjava.pro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.rxjava.pro.interceptors.MarvelInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MarvelInterceptorConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private MarvelInterceptor marvelInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("-----MarvelInterceptors added to the registry-----");
		registry.addInterceptor(marvelInterceptor);
	}

}
