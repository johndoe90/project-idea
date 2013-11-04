package com.phillip.idea.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
		          "com.phillip.idea.controller",
		          "com.phillip.idea.domain",
		          "com.phillip.idea.domain.dto",
		          "com.phillip.idea.miscellaneous",
		          "com.phillip.idea.service",
		          "com.phillip.idea.service.impl",
		          "com.phillip.idea.repository",
		          "com.phillip.idea.repository"})
public class TestConfig {

}
