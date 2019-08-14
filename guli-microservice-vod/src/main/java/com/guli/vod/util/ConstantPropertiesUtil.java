package com.guli.vod.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hyj
 * @since 2019/7/17
 */
//@PropertySource("classpath:配置文件名称")
//默认读取application.properties
// 在项目启动加载到spring容器中后，就会从默认配置文件中 读取配置值并赋值
@Component
//@PropertySource("classpath:application.properties")
public class ConstantPropertiesUtil implements InitializingBean {

	//取配置文件中的值
	@Value("${aliyun.vod.file.keyid}")
	private String keyId;

	@Value("${aliyun.vod.file.keysecret}")
	private String keySecret;

	@Value("${aliyun.vod.file.regionId}")
	private String regionId;

	//不能赋值给静态值
	public static String ACCESS_KEY_ID;
	public static String ACCESS_KEY_SECRET;
	public static String REGION_ID;

	//用spring的 InitializingBean 的 afterPropertiesSet 来初始化配置信息，
	// 这个方法将在所有的属性被初始化后调用。
	@Override
	public void afterPropertiesSet() throws Exception {
		//为常量赋值
		ACCESS_KEY_ID = keyId;
		ACCESS_KEY_SECRET = keySecret;
		REGION_ID= regionId;
	}
}
