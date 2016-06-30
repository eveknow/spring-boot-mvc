package com.mvmlabs.springboot;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "barEntityManagerFactory", transactionManagerRef = "barTransactionManager", basePackages = {
		"com.mvmlabs.springboot.repository" })
public class JpaDataConfig {

	@Bean(name = "barDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource barDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "barEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean barEntityManagerFactory(
			@Qualifier("barDataSource") DataSource barDataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(barDataSource);
		em.setPackagesToScan(new String[] { "com.mvmlabs.springboot.entity" });
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);
		em.setJpaVendorAdapter(vendorAdapter);
		
		return em;
	}

	@Bean(name = "barTransactionManager")
	public PlatformTransactionManager barTransactionManager(
			@Qualifier("barEntityManagerFactory") EntityManagerFactory barEntityManagerFactory) {
		return new JpaTransactionManager(barEntityManagerFactory);
	}

}