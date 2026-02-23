package com.sneha;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

@Configuration
public class ProtobufConfig {

    // This bean tells Spring how to turn JSON into Protobuf objects
    @Bean
    public ProtobufJsonFormatHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufJsonFormatHttpMessageConverter();
    }
}