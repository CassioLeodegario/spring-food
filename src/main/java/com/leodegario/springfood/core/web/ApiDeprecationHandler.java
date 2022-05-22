package com.leodegario.springfood.core.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiDeprecationHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (request.getRequestURI().startsWith("/v1/")) {
            response.addHeader("X-SpringFood-Deprecated",
                    "This API version is deprecated and will no longer be supported after 01/01/2023 " +
                            " please use the new version of the API");
        }

        return true;
    }
}
