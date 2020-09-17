package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.obs.services.ObsClient;

@Configuration
public class ObsConfiguration {

	@Bean
	public ObsClient obsClient(ObsProperties obsProperties) {
		return new ObsClient(obsProperties.accessKey, obsProperties.secretAccessKey, obsProperties.endPoint);
	}
}
