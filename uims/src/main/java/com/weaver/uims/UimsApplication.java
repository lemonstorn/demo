package com.weaver.uims;

import com.weaver.db.annotation.EnableWeaverDb;
import com.weaver.web.annotation.EnableWeaverWeb;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableWeaverDb
@EnableWeaverWeb
@SpringBootApplication
@MapperScan("com.weaver.uims.*.query")
public class UimsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UimsApplication.class, args);
    }

}
