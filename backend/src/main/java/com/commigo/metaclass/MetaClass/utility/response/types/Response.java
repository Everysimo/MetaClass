package com.commigo.metaclass.MetaClass.utility.response.types;

import com.commigo.metaclass.MetaClass.utility.response.AbstractResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<Type> extends AbstractResponse<Type>
{

    public Response(Type value,String message)
    {
        super(value,message);
    }

}
