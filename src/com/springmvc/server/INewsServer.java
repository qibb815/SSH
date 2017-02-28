package com.springmvc.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.model.News;

/** 
 * spring MVC����restful�ӿ� 
 *  
 * @author gaoweignag 
 * @since JDK1.7 
 */  
@Controller   
public interface INewsServer {
	
	@RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8")  
    public @ResponseBody  
    String hello();  
  
      
    @RequestMapping(value = "/say/{msg}", produces = "application/json;charset=UTF-8")  
    public @ResponseBody  
    String say(@PathVariable(value = "msg") String msg);  
  
      
    @RequestMapping(value = "/person/{id:\\d+}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")  
    public @ResponseBody  
    String getPerson(@PathVariable("id") String id);  
      
      
    @RequestMapping(value = "/person/{id:\\d+}", method = RequestMethod.DELETE)  
    public @ResponseBody Object deletePerson(@PathVariable("id") int id) ;  
      
    /** 
     * �Ƽ�ʹ�ã����ֿ��Խ������������ 
     * @param person 
     * @return 
     */  
    @RequestMapping(value = "/person", method = RequestMethod.POST)  
    public String addPerson(News news);  
      
      
    @RequestMapping(value = "/person", method = RequestMethod.PUT)  
    public @ResponseBody Object updatePerson(@RequestBody News news);  
}
