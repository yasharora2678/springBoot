package com.aop.programming.Aspect;

// import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
    @Before("execution(* com.aop.programming.Controller.EmployeeController.fetchEmployee())")
    public void beforeMethod() {
        System.out.println("Print before method aspect");
    }
    // This one is usign wildcard for matching any return type
    // @Before("execution(*
    // com.aop.programming.Controller.EmployeeController.fetchEmployee())")
    // public void beforeMethod() {
    // System.out.println("Print before method aspect");
    // }

    // This one is used for matching any method with single parameter string
    // @Before("execution(*
    // com.aop.programming.Controller.EmployeeController.*(String))")
    // public void beforeMethod() {
    // System.out.println("Print before method aspect");
    // }
    
    // This one is used for matching any method with single parameter
    // @Before("execution(*
    // com.aop.programming.Controller.EmployeeController.fetchEmployee(*))")
    // public void beforeMethod() {
    // System.out.println("Print before method aspect");
    // }

    // This one is used for matching any method with 0 or more parameter
    // @Before("execution(*
    // com.aop.programming.Controller.EmployeeController.fetchEmployee(..))")
    // public void beforeMethod() {
    // System.out.println("Print before method aspect");
    // }

    // This one is used for matching fetchEmployee method in aop.programming package
    // and subpackage classes
    // @Before("execution(* com.aop.programming..fetchEmployee(..))")
    // public void beforeMethod() {
    // System.out.println("Print before method aspect");
    // }

    // This one is used for matching any method in aop.programming package and
    // subpackage classes
    // @Before("execution(* com.aop.programming..*(..))")
    // public void beforeMethod() {
    // System.out.println("Print before method aspect");
    // }

    // This one will match all method in aop.programming controller
    @Before("execution(* com.aop.programming.Controller.EmployeeController.fetchEmployee())")
    public void withinMethod() {
        System.out.println("Print within method aspect");
    }

    // This one will match any method that is annotated with given annotation
    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void annotationMethod() {
        System.out.println("Print annotation method aspect");
    }

    // This one will match any method with these particular arguments
    @Before("args(String, int)")
    public void argsMethod() {
        System.out.println("Print args method aspect");
    }

    // This one will match method with these particular arguments and in this
    // specific annotation
    // @Before("@args(org.springframework.stereotype.Service)")
    // public void argumentMethod() {
    // System.out.println("Print args method aspect");
    // }

    // This one is using target for a particular instance
    @Before("target(com.aop.programming.Controller.EmployeeController)")
    public void targetMethod() {
        System.out.println("Print target method aspect");
    }

    @Pointcut("execution(* com.aop.programming.Controller.EmployeeController.fetchEmployee())")
    public void customPointName() {}

    @Before("customPointName()")
    public void customNameMethod() {
        System.out.println("Custom method aspect");
    }
// This is an around method used before execution and after execution
    // @Around("execution(* com.aop.programming.Controller.EmployeeController.fetchEmployee())")
    // public void aroundMethod() {
    //     System.out.println("Around method aspect");
    // }
}
