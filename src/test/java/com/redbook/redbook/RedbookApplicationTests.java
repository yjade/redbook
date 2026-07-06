package com.redbook.redbook;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.redbook.redbook.mapper")
class RedbookApplicationTests {

    @Test
    void contextLoads() {
    }

}
