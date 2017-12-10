package com.loki;

import com.loki.config.PBMessageConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author  by loki on 2017/9/17.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    PBMessageConverter protobufHttpMessageConverter() {
        return new PBMessageConverter();
    }

}
