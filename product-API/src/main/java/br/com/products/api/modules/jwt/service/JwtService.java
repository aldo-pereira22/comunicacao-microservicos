package br.com.products.api.modules.jwt.service;

import br.com.products.api.config.exception.ValidationException;
import br.com.products.api.modules.jwt.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class JwtService {

//    private static final String EMPTY_SPACE = " ";
//    private static final Integer TOKEN_INDEX  = 1;
//
//    private static final String BEARER = "bearer ";
//
//    @Value("${app-config.secrets.api-secret}")
//    private String apiSecret;
//
//    public void validateAuthorization(String token) throws AuthenticationException {
//
//        try{
//            var accessToken = extractToken(token);
//            var claims = Jwts.parserBuilder()
////                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
//                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
//                    .build()
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//            var user = JwtResponse.getUser(claims);
//            if(isEmpty(user) || isEmpty(user.getId())){
//                throw  new AuthenticationException("The user is not valid");
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
////            throw new AuthenticationException("Error while trying to proccess the access token");
//        }
//    }
//
//    private String extractToken(String token) throws AuthenticationException {
//        if(isEmpty(token)){
//
//            throw new AuthenticationException("The access token was not informed!");
//
//        }
//
//        if(token.contains(EMPTY_SPACE)){
//            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
//         }
//
//
//        return token;
//    }


    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateAuthorization(String token) {
        var accessToken = extractToken(token);
        try {
            var claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            var user = JwtResponse.getUser(claims);
            if (isEmpty(user) || isEmpty(user.getId())) {
                throw new AuthenticationException("The user is not valid.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                throw new AuthenticationException("Error while trying to proccess the Access Token.");
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            try {
                throw new AuthenticationException("The access token was not informed.");
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
