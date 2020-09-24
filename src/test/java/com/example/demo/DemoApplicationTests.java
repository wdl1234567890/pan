package com.example.demo;

import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	UserService userService;

//	@Test
//	void contextLoads() throws Exception {
//		List<String> warnings = new ArrayList<String>();
//		boolean overwrite = true;
//		File configFile = new File("generatorConfig.xml");
//		ConfigurationParser cp = new ConfigurationParser(warnings);
//		Configuration config = cp.parseConfiguration(configFile);
//		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
//		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//		myBatisGenerator.generate(null);
//
//	}

	@Test
	public void test() throws Exception {

		try {
			userService.importUsers(new FileInputStream("E:\\WorkSpace\\clone\\pan\\src\\main\\resources\\default.xlsx"),"default.xlsx");
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
