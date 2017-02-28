package com.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.model.News;

import net.sf.json.JSONObject;

@Controller
public class HelloWorld {
	
	private static final Logger logger = Logger.getLogger(HelloWorld.class);  
	 
    @RequestMapping("/helloworld")
    public String hello(){
        System.out.println("hello world");
        return "success";
    }
    
    @RequestMapping("/getnews")
    @ResponseBody
    public String getNews(HttpServletRequest request,HttpServletResponse response) {  
    	response.setContentType("text/html;charset=UTF-8");
    	response.addHeader("Access-Control-Allow-Origin","*"); 
        News news = new News();  
        news.setId("123123");  
        news.setTitle("");
        news.setContent("");
          
        JSONObject jsonObject = JSONObject.fromObject(news);  
        logger.info(jsonObject);  
        logger.info(jsonObject.toString()); 
        System.out.println(jsonObject.toString());
        return jsonObject.toString();  
    }  
    
}
