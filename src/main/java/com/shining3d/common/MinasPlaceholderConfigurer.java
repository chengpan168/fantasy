package com.shining3d.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class MinasPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	
	private static final String KEY_NAME_MINAS_URL = "minas.url";
	
	private String minasUrl;
	private String runMode;
	
	
	
	@Override
	protected Properties mergeProperties() throws IOException {
		Properties oldProps = super.mergeProperties();
		Properties props = System.getProperties();
		String propsRunMode = props.getProperty("runMode", AppConstants.DEFAULT_RUN_MODE);
		if(StringUtils.isBlank(this.runMode)){
			this.runMode = propsRunMode;
		}
		
		logger.debug("Start to execute placeholder replacement, runMode: " + this.runMode);
		if(StringUtils.isBlank(this.minasUrl)){
			this.minasUrl = props.getProperty(KEY_NAME_MINAS_URL);
		}
		
		String strFileContent = HttpUtils.doGet(minasUrl + this.runMode.toLowerCase(),"UTF-8",null);
		PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
		ByteArrayInputStream bis =  new ByteArrayInputStream(strFileContent.getBytes("UTF-8"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(bis)); 
		propertiesPersister.load(oldProps, bf);
		if(bis!=null){
			bis.close();
		}
		if(bf!=null){
			bf.close();
		}
		return oldProps;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			Properties mergedProps = mergeProperties();
			// Convert the merged properties, if necessary.
			convertProperties(mergedProps);
			// Let the subclass process the properties.
			processProperties(beanFactory, mergedProps);
		} catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
	}

	public void setMinasUrl(String minasUrl) {
		this.minasUrl = minasUrl;
	}

	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

}
