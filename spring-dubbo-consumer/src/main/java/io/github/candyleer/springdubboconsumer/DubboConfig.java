package io.github.candyleer.springdubboconsumer;

import io.github.candyleer.springdubboapi.HelloService;
import org.apache.dubbo.config.spring.ReferenceBean;
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
    public ReferenceBean<HelloService> helloServiceReferenceBean() {
        ReferenceBean<HelloService> referenceBean = new ReferenceBean<>();
        referenceBean.setInterface(HelloService.class.getName());
        LOGGER.info("[DubboConfig] --dubbo.monitor.skip:{}", skip);
        if (!skip) {
            referenceBean.setFilter("prometheus-consumer,jaeger-tracing");
        } else {
            referenceBean.setFilter("-prometheus-consumer,-jaeger-tracing");
        }
        return referenceBean;
    }
}
