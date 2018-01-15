package com.zgiot.app.server.config;

import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.AccessLogInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Value("${uploadFile.dir}")
    private String uploadFileUri;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessLogInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有/product/images/** 访问都映射到classpath:/product/images/ 目录下
        registry.addResourceHandler("/product/images/**").addResourceLocations("classpath:/product/images/");
        registry.addResourceHandler("/files/**").addResourceLocations("file:"+uploadFileUri + "/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public Converter<String, Date> addNewConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                try {
                    //进行日期转换
                    return new Date(Long.parseLong(source));

                } catch (Exception e) {
                    throw new SysException("日期转换出错",SysException.EC_UNKNOWN);
                }
            }
        };
    }
}

