package totalplay.snmpv2.com.configuracion;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "verticaEntityManagerFactory", transactionManagerRef = "verticaTransactionManager",
basePackages = {"totalplay.snmpv2.com.persistencia.vertica.repositorio"})
public class verticaDBConfig {
	
	@Autowired
	private Environment env;
	
	@Bean(name = "verticaDataSource")
	public DataSource verticaDatasource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(env.getProperty("dbvertica.datasource.url"));
		dataSource.setUsername(env.getProperty("dbvertica.datasource.username"));
		dataSource.setPassword(env.getProperty("dbvertica.datasource.password"));
		dataSource.setDriverClassName(env.getProperty("dbvertica.datasource.driverClassName"));
		
		return dataSource;
	}
	
	@Bean(name = "verticaEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(verticaDatasource());
		em.setPackagesToScan("totalplay.snmpv2.com.persistencia.vertica.entidades");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.show-sql", env.getProperty("dbvertica.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("dbvertica.jpa.database-platform"));
		
		em.setJpaPropertyMap(properties);
		
		return em;
		
	}
	
	@Bean(name = "verticaTransactionManager")
	public PlatformTransactionManager transactionManager() {
		System.out.println("Entrò");
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		return transactionManager;
	}
	

}
