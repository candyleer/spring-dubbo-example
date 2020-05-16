package io.github.candyleer.springdubboprovider.impl;

import io.github.candyleer.springdubboapi.HelloService;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service(filter = {"prometheus-provider", "jaeger-tracing"})
@org.springframework.stereotype.Service
public class HelloServiceImpl implements HelloService {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Map<String, String> hello() {
        Map<String, String> objectAttachments = RpcContext.getContext().getAttachments();
        objectAttachments.put("provider.name", applicationName);
        objectAttachments.put("provider.ip", NetUtils.getLocalHost());
        return objectAttachments;
    }
}
