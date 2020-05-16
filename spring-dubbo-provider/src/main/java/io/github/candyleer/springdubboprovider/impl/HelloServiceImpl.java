package io.github.candyleer.springdubboprovider.impl;

import io.github.candyleer.springdubboapi.HelloService;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service(filter = "prometheus-provider")
@org.springframework.stereotype.Service
public class HelloServiceImpl implements HelloService {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Map<String, Object> hello() {
        Map<String, Object> objectAttachments = RpcContext.getContext().getObjectAttachments();
        objectAttachments.put("provider.name", applicationName);
        objectAttachments.put("provider.ip", NetUtils.getLocalHost());
        return objectAttachments;
    }
}
