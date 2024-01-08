package com.commigo.metaclass.MetaClass.utility;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.ClientRuntimeException;
import jakarta.validation.*;
import jakarta.validation.Validator;

import java.util.*;

public class MapValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    public static boolean stanzaValidate(Map<String, Object> params) throws ClientRuntimeException {

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();

            try{
                Set<ConstraintViolation<Stanza>> violations =
                    validator.validateValue(Stanza.class, attributeName, attributeValue);

                if (!violations.isEmpty()) {
                    // Handle validation errors for the specific attribute
                    throw new ClientRuntimeException("Errore nella richiesta: "+ violations.iterator().next().getMessage());
                }
            }catch(IllegalArgumentException e){
                throw new ClientRuntimeException("Errore nella richiesta: L'attributo '"+
                        attributeName+ "' non è presente nell'entità Stanza");
            }catch (ValidationException ve){
                throw new ClientRuntimeException("Errore nella richiesta: L'attributo '"+
                        attributeName+ "' ha un valore che non rispetta il suo tipo di dato");
            }

/*            //controllo correttezza attributo
            if (!VALID_ATTRIBUTE_NAMES_STANZA.contains(attributeName)) {
                throw new ClientRuntimeException("Errore nella richiesta: L'attributo '"+
                        attributeName+ "' non è presente nell'entità Stanza");
            }*/

        }
        return true;

    }

    public static boolean utenteValidate(Map<String, Object> params) throws ClientRuntimeException {

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();

            try{
                Set<ConstraintViolation<Utente>> violations =
                        validator.validateValue(Utente.class, attributeName, attributeValue);

                if (!violations.isEmpty()) {
                    // Handle validation errors for the specific attribute
                    throw new ClientRuntimeException("Errore nella richiesta: "+ violations.iterator().next().getMessage());
                }
            }catch(IllegalArgumentException e){
                throw new ClientRuntimeException("Errore nella richiesta: L'attributo '"+
                        attributeName+ "' non è presente nell'entità Utente");
            }catch (ValidationException ve){
                throw new ClientRuntimeException("Errore nella richiesta: L'attributo '"+
                        attributeName+ "' ha un valore che non rispetta il suo tipo di dato");
            }

        }
        return true;

    }
}
