package com.custom.interceptor.Interceptors;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.custom.interceptor.Annotations.CustomAnnotation;

@Component
@Aspect
public class MyCustomInterceptor {
    @Around("@annotation(com.custom.interceptor.Annotations.CustomAnnotation)")
    public void invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Do Something before Actual Method");

        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        if(method.isAnnotationPresent(CustomAnnotation.class)){
            CustomAnnotation annotation = method.getAnnotation(CustomAnnotation.class);
            System.out.println("Name from annotation " + annotation.getClass());
        }

        joinPoint.proceed();

        System.out.println("Do somethign after actual method");
    }
}
