package com.springmvc.server.impl;


import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.model.News;
import com.springmvc.server.INewsServer;

import net.sf.json.JSONObject;

public class NewsServerImpl implements INewsServer{

	/** 日志实例 */  
    private static final Logger logger = Logger.getLogger(NewsServerImpl.class);  
    
    public @ResponseBody  
    String hello() {  
        logger.info("hello........");  
        return "你好！hello";  
    }  
  
    public @ResponseBody  
    String say(@PathVariable(value = "msg") String msg) {  
        return "{\"msg\":\"you say:'" + msg + "'\"}";  
    }  
  
    public @ResponseBody  
    String getPerson(@PathVariable("id") String id) {  
        logger.info("新闻id=" + id);  
        News news = new News();  
        news.setId(id);  
        news.setTitle("标题1111");
        news.setContent("内容23123123");
          
        JSONObject jsonObject = JSONObject.fromObject(news);  
        logger.info(jsonObject);  
        logger.info(jsonObject.toString());  
        return jsonObject.toString();  
    }  
      
    public Object deletePerson(@PathVariable("id") int id) {  
        logger.info("新闻id=" + id);  
        JSONObject jsonObject = new JSONObject();  
            jsonObject.put("msg", "删除人员信息成功");  
    return jsonObject;  
    }  
  
  
    public @ResponseBody String addPerson(@RequestBody News news) {  
        logger.info("新闻id=" + news.getId());  
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("msg", "新闻信息成功");  
        return jsonObject.toString();  
    }  
      
    public @ResponseBody Object updatePerson(@RequestBody News news) {  
        logger.info("更新人员信息id=" + news.getId());  
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("msg", "更新人员信息成功");  
        return jsonObject.toString();  
    }  

}
