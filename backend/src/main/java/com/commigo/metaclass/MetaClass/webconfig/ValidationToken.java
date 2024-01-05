package com.commigo.metaclass.MetaClass.webconfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
@Data
public class ValidationToken {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public boolean isTokenValid(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            return jwtTokenUtil.validateToken(token);
        }

        return false;
    }
}

