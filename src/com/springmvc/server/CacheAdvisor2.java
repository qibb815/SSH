package com.springmvc.server;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component("logAdvisor")
public class CacheAdvisor2 {

	public void doBefore(){
		System.out.println("do before ...");
		System.out.println("******************");
	}
	
	public void doAfter(){
		System.out.println("******************");
		System.out.println("do after ...");
	}

	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		// TODO Auto-generated method stub
		
	}
	
	public void pjp(ProceedingJoinPoint pjp){
		
	}
}
