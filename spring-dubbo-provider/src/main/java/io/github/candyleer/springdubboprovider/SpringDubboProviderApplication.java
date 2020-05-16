package io.github.candyleer.springdubboprovider;

import io.github.candyleer.springdubbotracing.config.MonitorConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MonitorConfig.class)
public class SpringDubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDubboProviderApplication.class, args);
    }

}
