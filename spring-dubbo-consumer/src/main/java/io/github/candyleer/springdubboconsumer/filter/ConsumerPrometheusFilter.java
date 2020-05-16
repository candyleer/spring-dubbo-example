package io.github.candyleer.springdubboconsumer.filter;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author lican
 */
@Activate(group = "consumer")
public class ConsumerPrometheusFilter implements Filter {

    private static final MeterRegistry METRICS = Metrics.globalRegistry;

    private static final String CONSUMER_METRIC_NAME = "dubbo_consumer";
    private static final String SERVICE = "service";
    private static final String METHOD = "method";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String interfaceName = invoker.getInterface().getCanonicalName();
        String methodName = invocation.getMethodName();
        Timer.Sample sample = Timer.start(METRICS);
        try {
            return invoker.invoke(invocation);
        } finally {
            sample.stop(METRICS.timer(CONSUMER_METRIC_NAME, SERVICE, interfaceName, METHOD, methodName));
        }
    }
}
