package com.bank.retail.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataBaseConfig {

	@Value("#{systemEnvironment['ENV']}")
	private String envLink;

	@Value("#{systemEnvironment['USR']}")
	private String username;

	@Value("#{systemEnvironment['DPW']}")
	private String password;

	@Value("#{systemEnvironment['CTO']}")
	private long conTimeout;

	@Value("#{systemEnvironment['MD']}")
	private int minIdle;

	@Value("#{systemEnvironment['MPS']}")
	private int maxPoolSize;

	@Value("#{systemEnvironment['ITO']}")
	private long idleTimeout;

	@Value("#{systemEnvironment['MLT']}")
	private int maxLifeTime;

	@Value("#{systemEnvironment['AT']}")
	private boolean autoCommit;

	@Value("#{systemEnvironment['SCHEMA']}")
	private String schema;

	@Primary
	@Bean(name = "ocsdb")
	DataSource datasource() {

		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(envLink);
		config.setUsername(username);
		config.setPassword(password);
		config.setConnectionTimeout(conTimeout);
		config.setMinimumIdle(minIdle);
		config.setMaximumPoolSize(maxPoolSize);
		config.setIdleTimeout(idleTimeout);
		config.setMaxLifetime(maxLifeTime);
		config.setAutoCommit(autoCommit);

		HikariDataSource dataSource = new HikariDataSource(config);
		return dataSource;

	}

}
