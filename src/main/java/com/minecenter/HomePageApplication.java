package com.minecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.minecenter.mapper")
public class HomePageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomePageApplication.class, args);
    }
}
