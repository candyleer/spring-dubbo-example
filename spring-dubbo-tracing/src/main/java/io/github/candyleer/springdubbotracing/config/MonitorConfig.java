package io.github.candyleer.springdubbotracing.config;


import io.github.candyleer.springdubbotracing.TracingInitializer;
import io.github.candyleer.springdubbotracing.filter.http.HttpTracingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "dubbo.monitor.skip", havingValue = "false")
public class MonitorConfig {

    @Bean
    public TracingInitializer tracingInitializer() {
        return new TracingInitializer();
    }

    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpTracingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("HttpTracingFilter");
        registration.setOrder(1);
        return registration;
    }

}
