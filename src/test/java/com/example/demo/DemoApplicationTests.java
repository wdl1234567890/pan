package com.example.demo;
import com.example.demo.domain.UserExample;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class DemoApplicationTests {

	@Autowired
	UserService userService;
	
	@Autowired
	UserMapper userMapper;
	
	@Test
	void contextLoads() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File("generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);

	}


	@Test
	public void test() throws Exception {

//		try {
//			userService.importUsers(new FileInputStream("E:\\WorkSpace\\clone\\pan\\src\\main\\resources\\default.xlsx"),"default.xlsx",new ArrayList<>());
//		}catch (Exception e){
//			e.printStackTrace();
//		}

//		UserExample userExample = new UserExample();
//		UserExample.Criteria criteria = userExample.createCriteria();
//		List<User> users = userMapper.selectByExample(userExample);
//		System.out.println(users.size());

		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();
		criteria.andDepartmentNotBetween(0,4);
		userMapper.deleteByExample(example);

	}

}
