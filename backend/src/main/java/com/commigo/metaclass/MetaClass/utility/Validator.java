package com.commigo.metaclass.MetaClass.utility;

public class Validator
{
    /**
     *
     * Controlla se un intero è positivo (x > 0 )
     *
     * @param value valore da controllare
     * @return restituisce vero se il numero intero è maggiore di 0
     */
    public static boolean isValid(int value)
    {
        return value > 0;
    }

    /**
     * Controlla se una stringa è valida
     *
     * @param str stringa da controllare
     * @return restituisce vero se la stringa non è nulla o vuota
     */
    public static boolean isValid(String str)
    {
        return str != null && !str.isEmpty();
    }

}
