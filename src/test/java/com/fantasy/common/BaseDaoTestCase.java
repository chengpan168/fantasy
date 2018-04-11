package com.fantasy.common;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml" })
public class BaseDaoTestCase {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setUp");
	}
}
