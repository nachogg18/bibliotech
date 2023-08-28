package com.bibliotech.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class PageUtil {

    private final HttpServletRequest servletRequest;

    public PageUtil(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public PageRequest pageRequest(Integer page) {
        return PageRequest.of(page - 1, 10);
    }

    public String getPreviousUrl(Integer page) {
        return servletRequest.getRequestURL().toString() + (page == 2 ? "" : "?page=" + (page - 1));
    }

    public String getNextUrl(Integer page) {
        return servletRequest.getRequestURL().toString() + "?page=" + (page + 1);
    }
}
