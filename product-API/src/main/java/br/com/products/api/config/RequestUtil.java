package br.com.products.api.config;

import br.com.products.api.config.exception.ValidationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static HttpServletRequest getCurrentRequest(){

        try{
            return ( (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new ValidationException("The current request could not be proccessed");
        }
    }
}
