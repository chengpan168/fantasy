package com.shining3d.common;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class VelocityService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostConstruct
	public void init(){
		Properties props = new Properties();
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		org.springframework.core.io.Resource resource = resourcePatternResolver.getResource("classpath:velocity.properties");
		try {
			props.load(resource.getInputStream());
		} catch (IOException e) {
			logger.error("velocity_init_error", e);
		}
//		props.put("input.encoding", "UTF-8");
//		props.put("output.encoding", "UTF-8");
//		props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
//		props.put("runtime.log.logsystem.log4j.category", "velocity_log");
//		props.put("file.resource.loader.class", "com.udai.maybach.biz.VelocityStringResourceLoader");
		
		Velocity.init(props);
	}
}
