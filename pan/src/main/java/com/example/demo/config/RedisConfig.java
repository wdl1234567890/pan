package com.example.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource(value="classpath:redisConfig.properties")
public class RedisConfig {
	@Value(value = "${redis.host}")
	private String host;
	@Value(value = "${redis.port}")
	private int port;
	@Value(value = "${redis.password}")
	private String password;
	@Value(value = "${redis.database}")
	private int database;
	@Value(value = "${redis.maxTotal}")
	private int maxTotal;
	@Value(value = "${redis.maxIdle}")
	private int maxIdle;
	@Value(value = "${redis.minIdle}")
	private int minIdle;
	@Value(value = "${redis.maxWaitMillis}")
	private int maxWaitMillis;
	@Value(value = "${redis.timeout}")
	private int timeout;
	@Value(value = "${redis.testOnBorrow}")
	private boolean testOnBorrow;

	/**
	 * spring容器创建一个内存中实例，名字和name值相同为：jedisPoolConfig
	 * @return
	 */
	@Bean(name = "jedisPoolConfig")
	public JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
	}

	/***
	 * 获得连接池对象
	 */
	@Bean(name = "jedisPool")
	public JedisPool getJedisPool(@Qualifier(value = "jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {
		return new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
	}
}
