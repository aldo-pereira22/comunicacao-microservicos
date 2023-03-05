package br.com.products.api.interception;

import br.com.products.api.modules.jwt.service.JwtService;
import feign.Request;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtService jwtService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler) throws AuthenticationException {
        if(isOptions(request)){
            return true;
        }
        var authorization = request.getHeader(AUTHORIZATION);

        jwtService.validateAuthorization(authorization);
        return true;
    }

    private boolean isOptions(HttpServletRequest request){
        return Request.HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
