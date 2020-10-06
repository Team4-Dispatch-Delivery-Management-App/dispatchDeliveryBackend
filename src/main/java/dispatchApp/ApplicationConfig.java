package dispatchApp;

import java.util.Properties;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import dispatchApp.utils.HeapClean;

@Configuration
@EnableWebMvc
@EnableScheduling
public class ApplicationConfig {

	private static final String USERNAME = "admin";
	private static final String PASSWORD = "";
	private static final String INSTANCE = "";
	private static final String PORT = "3306";
	private static final String DB_NAME = "dispatch";


	@Bean(name="sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("dispatchApp.model");
//		sessionFactory.setPackagesToScan("dispatchApp.utils");
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}
	
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		// change to your own RDS_Endpoint
		// change the username and password
		dataSource.setUrl("jdbc:mysql://"+ INSTANCE +":"+ PORT + "/" + DB_NAME + "?serverTimezone=UTC");
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);

		return dataSource;
	}
	
//	@Bean(name="dataSource")
//	public DataSource dataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://"
//				+ "delivery-instance.cottayujb1yg.us-east-2.rds.amazonaws.com"
//				+ ":3306/deliveryApp?serverTimeZone=UTC");
//		dataSource.setUsername("admin");
//		dataSource.setPassword("12345678");
//		return dataSource;
//	}
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(1024000);
		return multipartResolver;
	}
	
	private final Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return hibernateProperties;
	}
	
	@Autowired
	HeapClean heapClean;
	@Scheduled(fixedDelay = 20000)
	
	public void scheduleFixedDelayTask() {
		System.out.println("peek: " + heapClean.getCarrierpq().peek());
		System.out.println("size: " + heapClean.getCarrierpq().size());
		heapClean.check();
	    System.out.println(
	      "Fixed delay task - " + System.currentTimeMillis() / 1000);
	}
	
	
	
}