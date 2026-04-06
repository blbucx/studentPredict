package com.itshuai.pojo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.http.client.MultipartBodyBuilder;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(30000);

        RestTemplate restTemplate = new RestTemplate(factory);
        // 手动添加：JSON转换器 + 文件上传/表单转换器，兼顾所有场景
        restTemplate.setMessageConverters(Arrays.asList(
                new MappingJackson2HttpMessageConverter(), // 处理JSON
                new FormHttpMessageConverter() // 处理multipart/form-data和普通表单
        ));
        return restTemplate;
    }
}