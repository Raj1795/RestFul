package com.trios.collaborate.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.trios.collaborate")
public class WebResolver {

	  @Bean
	  public InternalResourceViewResolver getViewResolver()
	  {
		  InternalResourceViewResolver internalResourceViewResolver=new InternalResourceViewResolver();
		  internalResourceViewResolver.setPrefix("/WEB-INF");
		  internalResourceViewResolver.setSuffix(".jsp");
		  System.out.println("Resolver Created");
		  return internalResourceViewResolver;
		  
	  }
}