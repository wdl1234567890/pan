package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Component
@PropertySource("classpath:obs.properties")
public class ObsProperties {
	@Value("${obs.config.accessKey}")
	public String accessKey;
	@Value("${obs.config.secretAccessKey}")
	public String secretAccessKey;
	@Value("${obs.config.userName}")
	public String userName;
	@Value("${obs.config.endPoint}")
	public String endPoint;
}
