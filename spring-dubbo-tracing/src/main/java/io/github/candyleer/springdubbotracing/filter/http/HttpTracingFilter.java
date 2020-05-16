package io.github.candyleer.springdubbotracing.filter.http;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.TextMapAdapter;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static io.opentracing.propagation.Format.Builtin.HTTP_HEADERS;

public class HttpTracingFilter implements javax.servlet.Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpTracingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Span span = null;
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        try {
            Tracer tracer = GlobalTracer.get();
            Enumeration<String> headerNames = request.getHeaderNames();
            Map<String, String> headerPair = new HashMap<>(16);
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headerPair.put(headerName, headerValue);
            }
            Tracer.SpanBuilder spanBuilder = tracer.buildSpan(request.getRequestURI())
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
                    .withTag(Tags.HTTP_URL.getKey(), request.getRequestURL().toString())
                    .withTag(Tags.HTTP_METHOD.getKey(), request.getMethod())
                    .withTag(Tags.COMPONENT.getKey(), "SpringMVC");
            SpanContext extract = tracer.extract(HTTP_HEADERS, new TextMapAdapter(headerPair));
            if (extract != null) {
                spanBuilder = spanBuilder.asChildOf(extract);
            }
            span = spanBuilder.start();
            tracer.scopeManager().activate(span);
        } catch (Exception e) {
            LOGGER.error("trace error", e);
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (span != null) {
                int status = response.getStatus();
                if (status >= 400) {
                    span.setTag(Tags.ERROR, true);
                    span.setTag(Tags.HTTP_STATUS, status);
                }
                span.finish();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
