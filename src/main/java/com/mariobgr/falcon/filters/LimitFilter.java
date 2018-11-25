package com.mariobgr.falcon.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LimitFilter implements Filter {

    private int limit = 10;
    private int count;
    private static final Logger logger = LoggerFactory.getLogger(LimitFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse  response,
                         FilterChain chain) throws IOException, ServletException {

        try {

            boolean ok;

            increment();

            ok = count < limit;

            if (ok) {

                chain.doFilter(request, response);

            } else {

                HttpServletResponse res = (HttpServletResponse)response;
                res.setStatus(429);
                logger.error("Too many requests from IP " + request.getRemoteAddr());

            }

        } finally {

            decrement();

        }

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    private synchronized void increment() {
        count++;
    }

    private synchronized void decrement() {
        count--;
    }

}