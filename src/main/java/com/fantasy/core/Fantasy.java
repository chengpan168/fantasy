package com.fantasy.core;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Fantasy {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Fantasy.class);
        Set<Object> set = new HashSet<Object>(1);
        set.add("classpath:spring.xml");
        app.setSources(set);
        app.run(args);

    }
}
