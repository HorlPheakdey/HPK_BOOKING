package com.devcambodia.HPK_Booking.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.io.IOException;
@Slf4j
@Configuration
public class CustomCorFilter implements Filter {
    private final static String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
// → Which domains are allowed (e.g. "*" or "http://localhost:3000")
    private final static String ACCESS_CONTROL_ALLOW_METHOD = "Access-Control-Allow-Methods";
// → Allowed HTTP methods (e.g. "GET, POST, PUT, DELETE")
    private final static String ACCESS_CONTROL_ALLOW_HEADER = "Access-Control-Allow-Headers";
// → Allowed custom headers (e.g. "Authorization, Content-Type")
    private final static String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
// → How long the results of a preflight request can be cached (in seconds)
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.info("ServletRequest {}", servletRequest);
        log.info("ServletResponse {}", servletResponse);
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_METHOD, "*");
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_HEADER, "Authorization, Content-Type");
        httpServletResponse.setHeader(ACCESS_CONTROL_MAX_AGE, "3600");

        if(HttpMethod.OPTIONS.name().equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        log.info(servletRequest.toString());
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
