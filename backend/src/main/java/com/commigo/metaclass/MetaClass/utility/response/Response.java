package com.commigo.metaclass.MetaClass.utility.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<Type> {

    private Type successValue;

    private String responseMessage;

}