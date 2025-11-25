package com.annotation.async.Controller;

import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AsyncController {
    @GetMapping
    public void getAsyncMethod() {
        System.out.println("inside getAsync Method" + Thread.currentThread().getName());
        CompletableFuture<String> result = asyncMethod();
        String output;
        try {
            output = result.get();
            System.out.println("Result we got it - " + output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("myThreadPoolExecutor")             // Using completable future
    public CompletableFuture<String> asyncMethod() {
    System.out.println("Inside async Method " +
    Thread.currentThread().getName());
    try {
    Thread.sleep(10000);
    } catch (Exception e) {
    System.out.println("Not enough threads vacant");
    }
    return CompletableFuture.completedFuture("Async Task Completed");
    }

    // @GetMapping("/async")
    // @Async("myThreadPoolExecutor")
    // public void asyncMethod() {
    //     System.out.println("Inside async Method " + Thread.currentThread().getName());
    //     int i = 0;
    //     System.out.println(5/i);
    // }
}
