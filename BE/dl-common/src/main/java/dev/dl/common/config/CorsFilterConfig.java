package dev.dl.common.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import static dev.dl.common.constant.Constant.EMPTY_STRING;
import static dev.dl.common.constant.Constant.HYPHEN;
import static dev.dl.common.helper.ObjectHelper.isNotNullAndNotEmpty;
import static dev.dl.common.helper.ObjectHelper.isNullOrEmpty;

@Component
@Slf4j
@SuppressWarnings("ALL")
public class CorsFilterConfig implements Filter {

    private static final String REQUEST_ID = "request_id";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String requestId = UUID.randomUUID().toString().replace(HYPHEN, EMPTY_STRING);
        if (isNullOrEmpty(req.getAttribute(REQUEST_ID))) {
            req.setAttribute(REQUEST_ID, requestId);
            request.setAttribute(REQUEST_ID, requestId);
        }
        MDC.put("clientIP", req.getRemoteAddr());
        MDC.put("httpMethod", ((HttpServletRequest) req).getMethod());
        String appHost = InetAddress.getLocalHost().getHostAddress();
        MDC.put("serviceDomain", appHost);
        MDC.put("operatorName", ((HttpServletRequest) req).getRequestURI());
        MDC.put("requestId", requestId);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With, credential, X-XSRF-TOKEN");
        if (isNotNullAndNotEmpty(req.getAttribute(REQUEST_ID))) {
            response.setHeader("Request-Id", requestId);
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

}