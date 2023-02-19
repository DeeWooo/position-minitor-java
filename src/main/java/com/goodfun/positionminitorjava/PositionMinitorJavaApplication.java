package com.goodfun.positionminitorjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@EnableWebSocket
@EnableScheduling
@SpringBootApplication
public class PositionMinitorJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PositionMinitorJavaApplication.class, args);
    }


    // 配置 RetryTemplate
    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }


}
