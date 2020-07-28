package com.example.casshiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan
public class CasshirodemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasshirodemoApplication.class, args);
    }

}
