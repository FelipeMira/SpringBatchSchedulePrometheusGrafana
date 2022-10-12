package com.felipemira.batch.utils;

import com.felipemira.batch.dao.ProductCSVMapper;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.Duration;

@Aspect
@Component
public class LoggingAspect
{
    @Autowired
    private MeterRegistry meterRegistry;
    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    @Around("execution(* com.felipemira.batch.schedules..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        MultiTaggedCounter multiTaggedCounter = new MultiTaggedCounter("spring-batch-processor-counter", meterRegistry, "status");

        TaggedTimer taggedTimer = new TaggedTimer("spring-batch-processor", "status", meterRegistry);

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();


        //Log method execution time
        LOGGER.info("Execution time of " + className + "." + methodName + " "
                + ":: " + stopWatch.getTotalTimeMillis() + " ms");

        LOGGER.info("Return " + result);

        multiTaggedCounter.increment((((JobExecution) result).getExitStatus().toString().split(";")[0]).split("=")[1]);

        taggedTimer.getTimer((((JobExecution) result).getExitStatus().toString().split(";")[0]).split("=")[1]).record(Duration.ofMillis(stopWatch.getTotalTimeMillis()));

        return result;
    }
/*
    //@Pointcut("execution(* com.felipemira.batch.processors.ProductItemProcessor.process(..)) && args(productItem, *)")
    @Around("execution(* com.felipemira.batch.processors.ProductItemProcessor.process(..)) && args(productItem)")
    public void profileProductItemProcessor(ProceedingJoinPoint joinPoint, ProductCSVMapper productItem) throws Throwable
    {
        Object ignoredToStringResult = joinPoint.proceed();
        System.out.println(productItem.getName());

    }
    @Around("profileProductItemProcessor(productItem)")
    public void toLowerCase(ProceedingJoinPoint joinPoint, ProductCSVMapper productItem) throws Throwable {
        Object ignoredToStringResult = joinPoint.proceed();
        System.out.println("DateTime#toString() has been invoked: " + ignoredToStringResult);
    }
    */

    @Pointcut("execution(* com.felipemira.batch.processors.ProductItemProcessor.process(..)) && args(productItem)")
    public void profileProductItemProcessor(ProductCSVMapper productItem)
    {
        System.out.println(productItem.getName());
    }

    @Around("profileProductItemProcessor(productItem)")
    public Object capture(ProceedingJoinPoint thisJoinPoint, ProductCSVMapper productItem) throws Throwable {
        System.out.println(productItem);
        return thisJoinPoint.proceed();
    }

}
