package io.github.candyleer.springdubboprovider;

import io.github.candyleer.springdubboapi.DirectHelloService;
import io.github.candyleer.springdubboapi.HelloService;
import org.apache.dubbo.config.spring.ServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboConfig.class);

    @Value("${dubbo.monitor.skip}")
    private boolean skip;

    @Bean
    public ServiceBean<HelloService> helloServiceServiceBean(HelloService helloService) {
        ServiceBean<HelloService> serviceBean = new ServiceBean<>();
        serviceBean.setInterface(HelloService.class.getName());
        serviceBean.setRef(helloService);
        LOGGER.info("[DubboConfig] --dubbo.monitor.skip:{}", skip);
        if (!skip) {
            serviceBean.setFilter("prometheus-provider,jaeger-tracing");
        } else {
            serviceBean.setFilter("-prometheus-provider,-jaeger-tracing");
        }
        return serviceBean;
    }


    @Bean
    public ServiceBean<DirectHelloService> directHelloServiceServiceBean(DirectHelloService directHelloService) {
        ServiceBean<DirectHelloService> serviceBean = new ServiceBean<>();
        serviceBean.setInterface(DirectHelloService.class.getName());
        serviceBean.setRef(directHelloService);
        serviceBean.setFilter("-prometheus-provider,-jaeger-tracing");
        return serviceBean;
    }
}
