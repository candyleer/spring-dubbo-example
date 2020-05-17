package io.github.candyleer.springdubbotracing;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.jaegertracing.spi.Sampler;
import io.jaegertracing.spi.Sender;
import io.jaegertracing.thrift.internal.senders.UdpSender;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

public class TracingInitializer implements InitializingBean {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${jaeger.agent.host}")
    private String jaegerAgentHost;

    @Value("${jaeger.agent.sampler.ratio}")
    private Double samplerRatio;

    @Override
    public void afterPropertiesSet() throws Exception {
        final Sender sender = new UdpSender(jaegerAgentHost, UdpSender.DEFAULT_AGENT_UDP_COMPACT_PORT, 0);

        final RemoteReporter remoteReporter = new RemoteReporter.Builder()
                .withSender(sender)
                .build();

        final Sampler sampler = new ProbabilisticSampler(samplerRatio);

//        final Sampler sampler = new ConstSampler(true);

        final JaegerTracer jaegerTracer = new JaegerTracer
                .Builder(applicationName)
                .withReporter(remoteReporter)
                .withSampler(sampler)
                .build();
        //register to global
        GlobalTracer.registerIfAbsent(jaegerTracer);
    }

}
