package com.loki.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by loki on 2017/9/17.
 */
@RestController
public class HelloNettyController {

    private static final Logger logger = LoggerFactory.getLogger(HelloNettyController
    .class);

    @GetMapping("/hello")
    public String sayHello(@RequestParam("username") String username){
        return new String("Hello netty !"+username);
    }
}
