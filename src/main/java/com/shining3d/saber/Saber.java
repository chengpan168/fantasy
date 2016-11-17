package com.shining3d.saber;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@RestController
@EnableAutoConfiguration
public class Saber extends WebMvcConfigurerAdapter{
	
	public static void main(String[] args) throws Exception {
    		SpringApplication app = new SpringApplication(Saber.class);
        	app.setWebEnvironment(true); 
        	Set<Object> set = new HashSet<Object>(1);
        	set.add("classpath:spring.xml");
        	app.setSources(set);
        	app.run(args);

    	
    }    
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Saber.class);
	}
}
