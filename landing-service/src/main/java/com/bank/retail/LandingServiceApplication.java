package com.bank.retail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.*"})
@EntityScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.*"})
public class pls LandingServiceApplication
{
	 
	 	private static String keyStore = System.getenv("KEYSTORE");
	    private static String keyStorPwd = System.getenv("KEYSTORE_PWD");
	    private static String keyStoreType = System.getenv("KEYSTORE_TYPE");
	    private static String trustStore = System.getenv("TRUST_STORE");
	    private static String trustStorePwd = System.getenv("TRUST_STORE_PWD");


	public static void main(String[] args) 
	{
		  	System.setProperty("javax.net.ssl.keyStore", keyStore);
	        System.setProperty("javax.net.ssl.keyStorePassword", keyStorPwd);
	        System.setProperty("javax.net.ssl.keyStoreType", keyStoreType);
	        System.setProperty("javax.net.ssl.trustStore", trustStore);
	        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePwd);
	        SpringApplication.run(LandingServiceApplication.class, args);
	}

}
