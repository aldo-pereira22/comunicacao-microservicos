package br.com.products.api.interception;

import feign.Request;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if(isOptions(request)){
            return true;
        }
        var authorization = request.getHeader();
        return false;
    }

    private boolean isOptions(HttpServletRequest request){
        return Request.HttpMethod.OPTIONS.name().equals(request.getMethod())
    }
}
