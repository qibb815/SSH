package com.springmvc.server;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

@Component("logAdvisor2")
public class CacheAdvisor implements MethodBeforeAdvice{

	public void doBefore(){
		System.out.println("do before ...");
		System.out.println("******************");
	}
	
	public void doAfter(){
		System.out.println("******************");
		System.out.println("do after ...");
	}

	@Override
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		// TODO Auto-generated method stub
		
	}
}
