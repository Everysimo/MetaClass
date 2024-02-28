package com.commigo.metaclass.gestionestimaduratameeting.service;

import com.commigo.metaclass.entity.FeedbackMeeting;
import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestionemeeting.repository.FeedbackMeetingRepository;
import com.commigo.metaclass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.gestionestimaduratameeting.adapter.StimaDurataMeetingAdapter;
import com.commigo.metaclass.gestionestimaduratameeting.adapter.StimaDurataMeetingAdapterImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.FieldValueUtil;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service gestione stima meeting. */
@Service("GestioneStimaMeetingService")
public class GestioneStimaMeetingServiceImpl implements GestioneStimaMeetingService {

  /** Adapter per ottenere i risultati dell'agente intelligente. */
  private StimaDurataMeetingAdapter stimaProgressiAdapter;

  @Autowired private StanzaRepository stanzaRepository;
  @Autowired private StatoPartecipazioneRepository statoPartecipazioneRepository;
  @Autowired private FeedbackMeetingRepository feedbackMeetingRepository;
  private Evaluator evaluator;

  private static final String REGRESSOR_ROOT = System.getProperty("user.dir") + File.separator + "ModuloAI"
          + File.separator + "RegressoreDurataMeeting.pmml";

  /** constructor. */
  public GestioneStimaMeetingServiceImpl() {
    stimaProgressiAdapter = new StimaDurataMeetingAdapterImpl();
  }

  /**
   * Metodo che permette di visualizzare la durata di un meeting.
   *
   * @param idStanza id della stanza
   * @return valore double
   * @throws RuntimeException403 eccezione generata quando avviene un errore Client.
   * @throws ServerRuntimeException eccezione generata quando avviene un errore Server
   */
  @Override
  public Double getDurataMeeting(Long idStanza) throws RuntimeException403, ServerRuntimeException {
    // controllo la stanza se esiste
    Stanza s;
    if ((s = stanzaRepository.findStanzaById(idStanza)) == null) {
      throw new RuntimeException403("non è possibile effettuare la stima: stanza non trovata");
    }
    // mi recapito la lista di utenti in stanza
    List<Utente> users;
    if ((users = statoPartecipazioneRepository.findUtentiInStanza(s.getId())) == null) {
      throw new ServerRuntimeException(
          "non è possibile effettuare la stima: " + "errore nella ricerca dei partecipanti");
    }
    double mediaDuration = 0;

    for (Utente u : users) {
      mediaDuration += requestPrediction(u);
    }
    return mediaDuration / users.size();
  }

  /**
   * Metodo per preparare gli argomenti.
   *
   * @param input Mappa conententi valori con chiave una stringa
   * @return mappa con i valori del dataset.
   */
  private Map<FieldName, FieldValue> prepareArguments(Map<String, Object> input) {
    Map<FieldName, FieldValue> arguments = new HashMap<>();

    for (Map.Entry<String, Object> entry : input.entrySet()) {
      FieldName fieldName = FieldName.create(entry.getKey());
      Object value = entry.getValue();

      FieldValue fieldValue = FieldValueUtil.create(value);

      arguments.put(fieldName, fieldValue);
    }

    return arguments;
  }

  /**
   * Metodo che decodifica i risultato del modello di AI.
   *
   * @param arguments mappa di valori da dare al modello.
   * @return mappa con la predizione.
   */
  public Map<String, ?> predict(Map<FieldName, FieldValue> arguments) {
    Map<FieldName, ?> results = evaluator.evaluate(arguments);

    return EvaluatorUtil.decodeAll(results);
  }

  /**
   * Metodo che effettua la predizione tramite il modello.
   *
   * @param utente Istanza di un utente.
   * @return Valore di durata del meeting per l'utente.
   */
  private Double requestPrediction(Utente utente) {

    try (InputStream is = new FileInputStream(REGRESSOR_ROOT)) {
      evaluator = new LoadingModelEvaluatorBuilder().load(is).build();

      // calcolo l'età
      Period periodo = Period.between(utente.getDataDiNascita(), LocalDate.now());

      // conversione sesso
      int sex;
      if (utente.getSesso().equalsIgnoreCase("M")) {
        sex = 2;
      } else if (utente.getSesso().equalsIgnoreCase("F")) {
        sex = 1;
      } else {
        sex = 0;
      }

      // ricavo la media dei vari ImmersionLevel dell'utente
      List<FeedbackMeeting> feeds = feedbackMeetingRepository.findFeedbackMeetingByUtente(utente);
      double immersionLevelAverage =
              feeds.stream()
                      .mapToInt(
                              FeedbackMeeting::getImmersionLevel) // Estrae gli immersionLevel come IntStream
                      .average() // Calcola la media degli immersionLevel
                      .orElse(0.0); // Valore di default se la lista è vuota

      // ricavo la media dei motionSickness
      double motionSicknessAverage =
              feeds.stream()
                      .mapToInt(FeedbackMeeting::getMotionSickness) // Estrae motionSickness come IntStream
                      .average() // Calcola la media dei motionSickness
                      .orElse(0.0); // Valore di default se la lista è vuota

      // Esempio di utilizzo
      Map<String, Object> input = new HashMap<>();
      input.put("Age", periodo.getYears()); // Sostituisci con il valore effettivo
      input.put("Gender", sex); // Sostituisci con il valore effettivo (0"O", 1"F" o 2"M")
      input.put(
              "ImmersionLevel",
              (int) immersionLevelAverage); // Sostituisci con il valore effettivo (da 1 a 5)
      input.put(
              "MotionSickness",
              (int) motionSicknessAverage); // Sostituisci con il valore effettivo (da 1 a 10)

      Map<FieldName, FieldValue> arguments = prepareArguments(input);

      Map<String, ?> results = predict(arguments);

      // Stampa i risultati
      return (Double) results.get("y");

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }


  }

  /**
   * Metodo che richiama l'adapter per arricchire il dataset.
   *
   * @param u Istanza di Utente-
   * @param durata Durata del meeting dell'utente.
   * @param immersionLevel Livello di immersività dell'utente.
   * @param motionSickness Livello di fastidio.
   * @throws ServerRuntimeException Eccezione generata da errori server.
   */
  @Override
  public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel, int motionSickness)
      throws ServerRuntimeException {
    stimaProgressiAdapter.addUtenteInDataset(u, durata, immersionLevel, motionSickness);
  }
}
