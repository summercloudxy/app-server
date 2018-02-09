package com.zgiot.app.server.config;

import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.AccessLogInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //跨域设置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        List<String> list = new ArrayList<>();
        list.add("*");
        corsConfiguration.setAllowedOrigins(list);
    /*
    // 请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）
    */
        corsConfiguration.addAllowedOrigin("*");// 设置访问源地址
        corsConfiguration.addAllowedHeader("*");//设置访问源请求头
        corsConfiguration.addAllowedMethod("*");//设置访问源请求方法
        return corsConfiguration;
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}

