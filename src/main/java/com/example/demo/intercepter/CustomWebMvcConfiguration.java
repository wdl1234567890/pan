package com.example.demo.intercepter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
    * 配置类
 * @author 白开水
 *
 */
@Configuration
public class CustomWebMvcConfiguration implements WebMvcConfigurer{
	
	//实现注册拦截器的方法
	@Bean
	public LoginStateIntercepter loginStateInterceptor() {
		return new LoginStateIntercepter();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginStateInterceptor()).addPathPatterns("/api/v1/pri/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
