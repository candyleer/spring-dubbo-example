package io.github.candyleer.springdubboconsumer;


import io.github.candyleer.springdubboapi.DirectHelloService;
import io.github.candyleer.springdubboapi.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {


    @Autowired
    private HelloService helloService;

    @Autowired
    private DirectHelloService directHelloService;

    @RequestMapping("hello")
    public Map<String, String> hello() {
        return helloService.hello();
    }

    @RequestMapping("direct/hello")
    public Map<String, String> directHello() {
        return directHelloService.hello();
    }
}
