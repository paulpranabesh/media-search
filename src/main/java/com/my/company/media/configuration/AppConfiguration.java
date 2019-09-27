package com.my.company.media.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;

/**
 * Created by prpaul on 9/26/2019.
 */
@Configuration
@EnableCaching
public class AppConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // "text", "javascript" needed to be added because iTunes API response time is "text/javascript"
        converter.setSupportedMediaTypes(Collections.singletonList(new MediaType("text", "javascript", Charset.forName("UTF-8"))));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }

}
