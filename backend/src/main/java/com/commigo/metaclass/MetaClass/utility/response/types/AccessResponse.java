package com.commigo.metaclass.MetaClass.utility.response.types;

import com.commigo.metaclass.MetaClass.utility.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponse<Type> extends AbstractResponse<Type>
{

    private boolean isInAttesa;

    public AccessResponse(Type value, String message, boolean isInAttesa)
    {
        super(value,message);
        this.isInAttesa = isInAttesa;
    }
}
