package com.commigo.metaclass.MetaClass.utility.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponse<Type> {
  private Type value;
  String message;
}
