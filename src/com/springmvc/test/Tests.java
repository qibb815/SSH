package com.springmvc.test;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.springmvc.model.Role;
import com.springmvc.model.User;

public class Tests {

	private static SessionFactory sessionFactory; 
	
	private static HibernateTemplate hibernateTemplate;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		sessionFactory = new Configuration().configure().buildSessionFactory();
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:props/spring-mvc.xml"); 
		hibernateTemplate = (HibernateTemplate) ac.getBean("hibernateTemplate");
		hibernateTemplate.getSessionFactory().openSession().setFlushMode(FlushMode.COMMIT);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	public static void main(String[] args){
		//sessionFactory = new Configuration().configure().buildSessionFactory();
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:props/spring-mvc.xml"); 
		hibernateTemplate = (HibernateTemplate) ac.getBean("hibernateTemplate");
		hibernateTemplate.setFilterNames("myOpenSessionInViewFilter");
		System.out.println("Tests.main()");
	}
	
	@Test
	public void test() {
		
	}

	// 自动建表
    @Test
    public void testCreate() throws Exception {
        // 创建配置管理类对象
    	ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
    	Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
        // 加载主配置文件
        
        // 创建工具类对象
        SchemaExport export = new SchemaExport();
        export.create(EnumSet.of(TargetType.DATABASE), metadata);
    }
    
    @Test
    public void testSave(){
    	User user = new User();
    	user.setName("张三");
    	user.setAge(20);
    	user.setPhone("13155515151");
    	user.setAddress("珲春街");
    	
    	User user2 = new User();
    	user2.setName("李四");
    	user2.setAge(20);
    	user2.setPhone("13155515151");
    	user2.setAddress("数码路");
    	
    	Set<Role> roles = new HashSet<Role>();
    	for(int i=0;i<5;i++){
    		Role role = new Role();
        	role.setName("role");
        	role.getUsers().add(user); 
        	user.getRoles().add(role);
        	roles.add(role);
    	}

    	Set<Role> roles2 = new HashSet<Role>();
    	for(int i=0;i<5;i++){
    		Role role = new Role();
        	role.setName("role");
        	role.getUsers().add(user2);
        	user2.getRoles().add(role);
        	roles2.add(role);
    	}
    	
    	hibernateTemplate.save(user);
    	hibernateTemplate.save(user2);
    	/*Iterator<Role> iRole = roles.iterator();
    	while (iRole.hasNext()) {
			Role role = (Role) iRole.next();
			hibernateTemplate.save(role);
		}
    	Iterator<Role> iRole2 = roles2.iterator();
    	while (iRole2.hasNext()) {
			Role role = (Role) iRole2.next();
			hibernateTemplate.save(role);
		}*/
//    	Session session = sessionFactory.openSession();
//    	session.beginTransaction();
//    	session.save(user);
//    	session.save(user2);
//    	
//    	session.save(roles);
//    	session.save(roles2);
//    	session.getTransaction().commit();
//    	session.close();
    }
    
    @Test
    public void testGetCache(){
    	
//    	Session session = sessionFactory.openSession();
//    	session.beginTransaction();
//    	User user = session.get(User.class,4);
//    	System.out.println(user.getName());
//    	session.getTransaction().commit();
//    	session.close();
//    	
//    	Session session2 = sessionFactory.openSession();
//    	session2.beginTransaction();
//    	User user2 = session2.get(User.class,4);
//    	System.out.println(user2.getName());
//    	session2.getTransaction().commit();
//    	session2.close();
    	User user =  hibernateTemplate.get(User.class, 8);
    	System.out.println(user.getName());
    }
}
