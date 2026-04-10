//package com.sneha;
//
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.Gauge;
//import io.micrometer.core.instrument.MeterRegistry;
//
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Service
//public class Metric {
//
//     private Counter registerUserCounter;
//     private Counter validateUserCounter;
//     private Counter getUserCounter;
//    private final AtomicInteger activeUserCreationRequests;
//
//    Metric(MeterRegistry meterRegistry){
//        this.registerUserCounter = Counter.builder("users_created")
//                .description("Number of users created")
//                .register(meterRegistry);
//
//        this.validateUserCounter = Counter.builder("validate_users_count").description("No of users validated").register(meterRegistry);
//        this.getUserCounter = Counter.builder("get_users_count").description("No of get users calls").register(meterRegistry);
//        activeUserCreationRequests = new AtomicInteger(0);
//
//    }
//
//    void registerUser(){
//        registerUserCounter.increment();
//    }
//
//    void validateUser(){
//        validateUserCounter.increment();
//    }
//
//    void getUsers(){
//        getUserCounter.increment();
//    }
//}
