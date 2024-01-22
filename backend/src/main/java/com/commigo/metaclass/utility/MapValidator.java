package com.commigo.metaclass.utility;

import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.exceptions.RuntimeException403;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/** Classe di validazione. */
@Component
@NoArgsConstructor
public class MapValidator {

  private static final Validator validator =
      Validation.buildDefaultValidatorFactory().getValidator();

  /**
   * Validatore stanza.
   *
   * @param params mappa con i dati stanza.
   * @return conferma validazione
   * @throws RuntimeException403 eccezione di errori nella richiesta.
   */
  public static boolean stanzaValidate(Map<String, Object> params) throws ClientRuntimeException {

    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String attributeName = entry.getKey();
      Object attributeValue = entry.getValue();

      try {
        Set<ConstraintViolation<Stanza>> violations =
            validator.validateValue(Stanza.class, attributeName, attributeValue);

        if (!violations.isEmpty()) {
          // Handle validation errors for the specific attribute
          throw new ClientRuntimeException(
              "Errore nella richiesta: " + violations.iterator().next().getMessage());
        }
      } catch (IllegalArgumentException e) {
        throw new ClientRuntimeException(
            "Errore nella richiesta: L'attributo '"
                + attributeName
                + "' non è presente nell'entità Stanza");
      } catch (ValidationException ve) {
        throw new ClientRuntimeException(
            "Errore nella richiesta: L'attributo '"
                + attributeName
                + "' ha un valore che non rispetta il suo tipo di dato");
      }
    }
    return true;
  }

  /**
   * Validatore utente.
   *
   * @param params mappa con i dati utente.
   * @return conferma validazione
   * @throws RuntimeException403 eccezione di errori nella richiesta.
   */
  public static boolean utenteValidate(Map<String, Object> params) throws RuntimeException403 {

    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String attributeName = entry.getKey();
      Object attributeValue = entry.getValue();

      try {

        if (attributeName.equalsIgnoreCase("dataDiNascita")) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

          try {
            LocalDate data = LocalDate.parse((CharSequence) attributeValue, formatter);

            // Creare un DateTimeFormatter per il formato di output
            DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Formattare la data di uscita nel nuovo formato
            String outputDate = data.format(formatterOutput);

            params.put(attributeName, outputDate);

          } catch (DateTimeParseException e) {
            throw new RuntimeException403(
                "Errore nella richiesta: Formato della data di nascita non valido. "
                    + "Formato richiesto: MM/dd/yyyy");
          }
        } else {
          Set<ConstraintViolation<Utente>> violations =
              validator.validateValue(Utente.class, attributeName, attributeValue);

          if (!violations.isEmpty()) {
            // Handle validation errors for the specific attribute
            throw new RuntimeException403(
                "Errore nella richiesta: " + violations.iterator().next().getMessage());
          }
        }

      } catch (IllegalArgumentException e) {
        throw new RuntimeException403(
            "Errore nella richiesta: L'attributo '"
                + attributeName
                + "' non è presente nell'entità Utente");
      } catch (ValidationException ve) {
        throw new RuntimeException403(
            "Errore nella richiesta: L'attributo '"
                + attributeName
                + "' ha un valore che non rispetta il suo tipo di dato");
      }
    }
    return true;
  }
}
