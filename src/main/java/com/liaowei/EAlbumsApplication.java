package com.liaowei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(value = "classpath:kaptcha.xml")
public class EAlbumsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EAlbumsApplication.class, args);
    }

}
