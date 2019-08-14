package com.guli.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//@EnableEurekaClient默认自动配置
//@EnableDiscoveryClient和@EnableEurekaClien都是能够让注册中心能够发现，扫描到该服务。
//@EnableEurekaClient只适用于Eureka作为注册中心，@EnableDiscoveryClient 可以是其他注册中心。
//从Spring Cloud Edgware开始，@EnableDiscoveryClient 或@EnableEurekaClient 可省略
//只需加上相关依赖，并进行相应配置，即可将微服务注册到服务发现组件上。
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
//扫描当前包和子包
//只是没有好的全局异常处理场景
@SpringBootApplication
@ComponentScan(basePackages = {"com.guli.core.handler","com.guli.edu"})
public class GuliEduApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuliEduApplication.class, args);
    }

}
