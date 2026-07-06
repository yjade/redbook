package com.redbook.redbook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.redbook.redbook.mapper")
public class RedbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedbookApplication.class, args);
    }

}
