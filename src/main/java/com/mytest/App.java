package com.mytest;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ApplicationPath("/")
public class App extends ResourceConfig {

    @PostConstruct
    public void initialize() {
        register(ProtobufMessageBodyProvider.class);
        register(HelloAPI.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
