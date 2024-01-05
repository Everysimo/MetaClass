package com.commigo.metaclass.MetaClass.utility.response.types;

import com.commigo.metaclass.MetaClass.utility.response.AbstractResponse;
import lombok.Data;

@Data
public class LoginResponse<Type> extends AbstractResponse<Type> {

    private String token;
    public LoginResponse(Type value,String message, String token)
    {
        super(value,message);
        this.token = token;
    }
}
