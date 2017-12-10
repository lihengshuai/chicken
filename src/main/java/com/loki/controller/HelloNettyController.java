package com.loki.controller;

import com.loki.Utils;
import com.loki.bean.PersonProto.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by loki on 2017/9/17.
 */
@RestController
public class HelloNettyController {

    private static final Logger logger = LoggerFactory.getLogger(HelloNettyController
    .class);

    final static int BUFFER_SIZE = 4096;

    @PostMapping(value = "/person")
    @ResponseBody
    public  byte[] sayHello(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream inputStream = request.getInputStream();

        byte[] bytes = Utils.InputStreamTOArray(inputStream);

        Utils.sys(bytes);

        byte[] copyOfRange = Arrays.copyOfRange(bytes,8 , bytes.length);
        Utils.sys(copyOfRange);


        Stream.of(copyOfRange).forEach(t -> logger.info("{}"+t));


        Person person = Person.parseFrom(Utils.byteTOInputStream(copyOfRange));
        logger.info("person =={}",person);
        inputStream.close();
        return bytes;
    }

    @GetMapping("/person")
    public void download(HttpServletResponse response) throws IOException{
        Person person = Person.newBuilder()
                .setId(1234)
                .setName("Loki")
                .build();
        response.setContentType("application/x-protobuf");
        OutputStream outputStream = response.getOutputStream();
        person.writeTo(outputStream);
        outputStream.flush();
        outputStream.close();
    }


    @PostMapping("/first")
    public void sayFirst(){
        logger.info("this is first !");
//        return "hhhh";
    }
}
