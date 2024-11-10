package com.kob.matchingsystem.config;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> ipAddressFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new IpAddressFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("IpAddressFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
