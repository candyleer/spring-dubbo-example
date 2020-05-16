package io.github.candyleer.springdubboconsumer;


import io.github.candyleer.springdubboapi.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    @Reference(filter = {"prometheus-consumer", "jaeger-tracing"})
    private HelloService helloService;

    @RequestMapping("hello")
    public Map<String, String> hello() {
        return helloService.hello();
    }
}
