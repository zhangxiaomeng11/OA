package com.tz.oa.framework.util;

import org.activiti.engine.ProcessEngine;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试activiti引擎是否托管成功
 * @author Administrator
 *
 */
public class ActivitiEngineTest {

	@Test
	public void testActivitiEngine(){
		ApplicationContext ac
							 = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		ProcessEngine processEngine = (ProcessEngine) ac.getBean("processEngine");
		System.out.println(processEngine);
	}
}
