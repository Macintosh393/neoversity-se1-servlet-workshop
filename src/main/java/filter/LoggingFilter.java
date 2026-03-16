package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String method = req.getMethod();
        String uri = req.getRequestURI();

        Long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        Long endTime = System.currentTimeMillis();
        Long duration = endTime - startTime;

        String statusCode = String.valueOf(resp.getStatus());
        System.out.println("Request: " + method + " " + uri +
                " - Duration: " + duration +
                "ms - Status: " + statusCode);
    }
}
