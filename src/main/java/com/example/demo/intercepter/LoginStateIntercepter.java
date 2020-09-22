package com.example.demo.intercepter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.vo.JsonData;
import com.example.demo.utils.UserTokenVo;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class LoginStateIntercepter implements HandlerInterceptor{
	
	@Autowired
	private JedisPool jedisPool;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("token");
		if(token==null) {
			this.renderJson(response, JsonData.buildError(-2,"非法访问"));
			return false;
		}
		
		//获取redis数据池连接
		Jedis jedis = jedisPool.getResource();
		String tokenValue = jedis.get(token);
		
		
		//检测token是否为空
		if(tokenValue==null) {
			this.renderJson(response, JsonData.buildError(-3, "请重新登陆"));
			jedis.close();
			return false;
		}
		//检测有效时间（>=0或为-1）
		Long ttl = jedis.ttl(token);
		if(ttl<-1) {
			this.renderJson(response, JsonData.buildError(-4, "登陆超时"));
			jedis.close();
			return false;
		}
		
		//未过期，仍处于登陆状态
		ObjectMapper mapper = new ObjectMapper();
		//反序列化，读取token内的存放的有效时间
		UserTokenVo tokenVo = mapper.readValue(tokenValue, UserTokenVo.class);
		jedis.expire(token, tokenVo.getValidTime());
		jedis.expire(tokenVo.getUser().getId().toString(), tokenVo.getValidTime());
		jedis.close();
		
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
	/**
	 * 将json数据转换成字符串打印给客户端
	 * @param response
	 * @param jsondata
	 */
	private void renderJson(HttpServletResponse response,JsonData jsondata) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		try {
			PrintWriter writer = response.getWriter();
			ObjectMapper mapper=new ObjectMapper();
			writer.print(mapper.writeValueAsString(jsondata));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LoginStateIntercepter() {
		super();
	}
	

}
