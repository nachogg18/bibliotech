package com.bibliotech.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class  TransactionFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        LOG.info(
                "Starting a transaction for request : {} - {}",
                req.getMethod(), req.getRequestURI());

        chain.doFilter(request, response);
        LOG.info(
                "Committing a transaction for req : {} - {}",
                req.getMethod(), req.getRequestURI());
    }
}