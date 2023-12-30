package com.commigo.metaclass.MetaClass.utility;

public class Validator
{
    /**
     * Convalida una valore intero controllando che sia positivo maggiore di 0
     *
     */
    public static boolean isValid(int value)
    {
        return value > 0;
    }

    /**
     * Convalida una stringa
     *
     */
    public static boolean isValid(String str)
    {
        return str != null && !str.isEmpty();
    }

}
