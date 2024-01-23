package com.commigo.metaclass.utility;

import com.commigo.metaclass.entity.Meeting;
import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.ClientRuntimeException;
import com.commigo.metaclass.exceptions.RuntimeException403;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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

  /**
   * Metodo che mi valida gli attributi del meeting presi da una mappa.
   *
   * @param params mappa di attributi
   * @return controllo di validazione avvenuta con successo
   * @throws ClientRuntimeException errore di bad request
   */
  public static boolean meetingValidate(Map<String, Object> params) throws ClientRuntimeException {

    List<LocalDateTime> dates = new ArrayList<>();
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String attributeName = entry.getKey();
      Object attributeValue = entry.getValue();

      try {
        if (!attributeName.equalsIgnoreCase("nome")) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

          try {
            dates.add(LocalDateTime.parse((CharSequence) attributeValue, formatter));
            System.out.println(dates.get(0));
          } catch (DateTimeParseException e) {
            throw new ClientRuntimeException(
                "Errore nella richiesta: L'attributo '"
                    + attributeName
                    + "' Formato richiesto: yyyy-MM-dd HH:mm");
          }

        } else {
          Set<ConstraintViolation<Meeting>> violations =
              validator.validateValue(Meeting.class, attributeName, attributeValue);

          if (!violations.isEmpty()) {
            // Handle validation errors for the specific attribute
            throw new ClientRuntimeException(
                "Errore nella richiesta: " + violations.iterator().next().getMessage());
          }
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
    // controllo l'ordine delle date
    if (isStartDateBeforeEndDate(dates)) {
      throw new ClientRuntimeException(
          "Errore nella richiesta: inizio deve essere minore di fine o viceversa");
    }
    return true;
  }

  private static boolean isStartDateBeforeEndDate(List<LocalDateTime> dates) {
    // La validazione sarà passata solo se la data di inizio è precedente a quella di fine
    return dates.get(0) == null || dates.get(1) == null || !dates.get(0).isBefore(dates.get(1));
  }
}
