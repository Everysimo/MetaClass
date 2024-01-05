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
    private String token;

    public boolean isTokenValid(HttpServletRequest request) {
        this.token = request.getHeader("Authorization");

        if (this.token != null && this.token.startsWith("Bearer ")) {
            this.token = this.token.substring(7);

            return jwtTokenUtil.validateToken(this.token);
        }

        return false;
    }
}

