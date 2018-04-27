package com.mytest;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

import com.mytest.proto.HelloReply;
import com.mytest.proto.HelloRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class HelloAPITest {
    
    private final RestTemplate restTemplate = new RestTemplate(Arrays.asList(new ProtobufHttpMessageConverter()));

    @LocalServerPort
    private int port;

    @Test
    public void testSayHello() {

        HelloReply reply = restTemplate.getForObject("http://127.0.0.1:" + port + "/hello", HelloReply.class);

        assertEquals("Hello world!", reply.getMessage());
    }

    @Test
    public void testReplyToGreeting() {

        HelloRequest request = HelloRequest.newBuilder().setName("Stack Overflow").build();
        
        HelloReply reply = restTemplate.postForObject("http://127.0.0.1:" + port + "/hello", request, HelloReply.class);

        assertEquals("Hello Stack Overflow!", reply.getMessage());
    }
}
