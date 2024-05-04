package org.teamchallenge.bookshop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public BookMapper bookMapper() {
        return BookMapper.BOOK_MAPPER;
    }

}