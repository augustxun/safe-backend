package com.augustxun.safe;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@EnableOpenApi
@MapperScan(basePackages = "com.augustxun.safe.mapper")
public class SAFEApplication {
    public static void main(String[] args) {
        SpringApplication.run(SAFEApplication.class, args);
    }
}
