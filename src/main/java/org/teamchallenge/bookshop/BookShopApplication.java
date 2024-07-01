package org.teamchallenge.bookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = "org.teamchallenge")
public class BookShopApplication  {
	public static void main(String[] args)   {
		SpringApplication.run(BookShopApplication.class, args);
	}
}
