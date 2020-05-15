package io.github.candyleer.springdubboprovider;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping("/health")
    public String health() {
        return "200";
    }

    @RequestMapping("/ready")
    public String ready() {
        return "200";
    }
}
