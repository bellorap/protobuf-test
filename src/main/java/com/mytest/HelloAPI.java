package com.mytest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.mytest.proto.HelloReply;
import com.mytest.proto.HelloRequest;

@Path("/")
public class HelloAPI {

    @GET
    @Path("/hello")
    @Produces(ProtobufMessageBodyProvider.MEDIA_TYPE)
    public HelloReply sayHello() {
        return HelloReply.newBuilder().setMessage("Hello world!").build();
    }
    
    @POST
    @Path("/hello")
    @Consumes(ProtobufMessageBodyProvider.MEDIA_TYPE)
    @Produces(ProtobufMessageBodyProvider.MEDIA_TYPE)
    public HelloReply replyToGreeting(HelloRequest helloRequest) {
        String message = String.format("Hello %s!", helloRequest.getName());
        return HelloReply.newBuilder().setMessage(message).build();
    }
}
