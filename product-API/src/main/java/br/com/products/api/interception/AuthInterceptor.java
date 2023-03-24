package br.com.products.api.interception;

import br.com.products.api.config.exception.ValidationException;
import br.com.products.api.modules.jwt.service.JwtService;
import feign.Request;
import io.jsonwebtoken.lang.Objects;
import static org.springframework.util.ObjectUtils.isEmpty;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";

    @Autowired
    private JwtService jwtService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler) throws AuthenticationException {
        if(isOptions(request)){
            return true;
        }
        if(isEmpty(request.getHeader(TRANSACTION_ID))) {
            throw new ValidationException("The transactionid header is required");
        }
        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validateAuthorization(authorization);
        request.setAttribute("serviceid", UUID.randomUUID().toString());
        return true;
    }

    private boolean isOptions(HttpServletRequest request){
        return Request.HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
