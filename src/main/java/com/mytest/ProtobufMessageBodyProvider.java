package com.mytest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.google.protobuf.Message;

/**
 * A message body reader and writer implementation based on DropWizard's
 * <a href=
 * "https://github.com/dropwizard/dropwizard-protobuf/blob/master/src/main/java/io/dropwizard/jersey/protobuf/ProtocolBufferMessageBodyProvider.java">
 * ProtocolBufferMessageBodyProvider</a>.
 */
@Provider
@Consumes(ProtobufMessageBodyProvider.MEDIA_TYPE)
@Produces(ProtobufMessageBodyProvider.MEDIA_TYPE)
@Component
public class ProtobufMessageBodyProvider implements MessageBodyReader<Message>, MessageBodyWriter<Message> {
    
    public static final String MEDIA_TYPE = "application/x-protobuf";
    
    private final Map<Class<Message>, Method> methodCache = new ConcurrentHashMap<>();
    
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }
    
    @Override
    public Message readFrom(
            Class<Message> type,
            Type genericType,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream)
            throws IOException {
        
        try {
            Method newBuilder = methodCache.computeIfAbsent(type, t -> {
                try {
                    return t.getMethod("newBuilder");
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(e);
                }
            });
            
            Message.Builder builder = (Message.Builder) newBuilder.invoke(type);
            return builder.mergeFrom(entityStream).build();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public long getSize(Message message, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return message.getSerializedSize();
    }
    
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }
    
    @Override
    public void writeTo(
            Message message,
            Class<?> type,
            Type genericType,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream)
            throws IOException {
        message.writeTo(entityStream);
    }
}
