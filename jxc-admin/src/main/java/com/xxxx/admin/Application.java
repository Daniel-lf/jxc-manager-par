package com.xxxx.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xxxx.admin.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
      /*  System.out.println(new BCryptPasswordEncoder().encode("4321"));*/
    }
}
