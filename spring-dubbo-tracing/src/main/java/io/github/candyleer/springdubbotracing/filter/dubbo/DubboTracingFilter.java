package io.github.candyleer.springdubbotracing.filter.dubbo;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.TextMapAdapter;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.HashMap;
import java.util.Map;

import static io.opentracing.propagation.Format.Builtin.TEXT_MAP;


@Activate(group = {"consumer", "provider"})
public class DubboTracingFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        boolean isConsumer = rpcContext.isConsumerSide();
        URL requestURL = invoker.getUrl();

        final String host = requestURL.getHost();
        final int port = requestURL.getPort();
        Tracer tracer = GlobalTracer.get();
        Span span;

        Map<String, String> attachments = rpcContext.getAttachments();
        if (isConsumer) {
            //consumer
            span = tracer.buildSpan(generateOperationName(requestURL, invocation))
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
                    .start();
            tracer.scopeManager().activate(span);
            SpanContext context = tracer.scopeManager().activeSpan().context();
            HashMap<String, String> map = new HashMap<>();

            tracer.inject(context, TEXT_MAP, new TextMapAdapter(map));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                attachments.put(entry.getKey(), entry.getValue());
            }
            span.setTag(Tags.SPAN_KIND.getKey(), "client");
            span.setTag(Tags.PEER_HOST_IPV4.getKey(), host + ":" + port);
        } else {
            //provider
            Tracer.SpanBuilder builder = tracer.buildSpan(generateOperationName(requestURL, invocation))
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER);
            SpanContext extract = tracer.extract(TEXT_MAP, new TextMapAdapter(attachments));
            if (extract != null) {
                builder = builder.asChildOf(extract);
            }
            span = builder.start();
            tracer.scopeManager().activate(span);
            span.setTag(Tags.PEER_HOST_IPV4.getKey(), rpcContext.getRemoteAddressString());
        }
        try {
            return invoker.invoke(invocation);
        } catch (Exception e) {
            span.setTag(Tags.ERROR.getKey(), true);
            throw new RpcException(e);
        } finally {
            span.finish();
        }
    }


    private String generateOperationName(URL requestUrl, Invocation invocation) {
        StringBuilder operationName = new StringBuilder();
        operationName.append(requestUrl.getPath());
        operationName.append(".").append(invocation.getMethodName()).append("(");
        for (Class<?> classes : invocation.getParameterTypes()) {
            operationName.append(classes.getSimpleName()).append(",");
        }

        if (invocation.getParameterTypes().length > 0) {
            operationName.delete(operationName.length() - 1, operationName.length());
        }

        operationName.append(")");

        return operationName.toString();
    }
}
