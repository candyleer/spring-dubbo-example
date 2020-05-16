package io.github.candyleer.springdubboconsumer;

import io.github.candyleer.springdubbotracing.config.MonitorConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MonitorConfig.class)
public class SpringDubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDubboConsumerApplication.class, args);
    }

}
