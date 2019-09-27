package com.my.company.media.aop;

import com.my.company.media.metric.ApiResponseMetricEndPoint;
import com.my.company.media.metric.ExternalApi;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * Created by prpaul on 9/27/2019.
 */
@EnableAspectJAutoProxy
@Aspect
@Component
public class ResponseTimeCalculationAspect {
    @Autowired
    ApiResponseMetricEndPoint metricEndPoint;

    @Around("execution(* retrieveBook(..))")
    public Object aroundGoogleBookApi(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = timeStamp();
        Object result = joinPoint.proceed();
        metricEndPoint.receiveAccessData(ExternalApi.GOOGLE_BOOK_API, timeElapsed(startTime));
        return result;
    }

    @Around("execution(* retrieveAlbum(..))")
    public Object aroundITunesApi(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = timeStamp();
        Object result = joinPoint.proceed();
        metricEndPoint.receiveAccessData(ExternalApi.ITUNES_MUSIC_API, timeElapsed(startTime));
        return result;
    }

    private Long timeStamp(){
        return System.currentTimeMillis();
    }

    private Long timeElapsed(Long start){
        return System.currentTimeMillis() - start;
    }
}
