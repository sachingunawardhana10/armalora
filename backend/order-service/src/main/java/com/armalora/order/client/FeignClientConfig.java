package com.armalora.order.client;

import feign.Logger;

import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}