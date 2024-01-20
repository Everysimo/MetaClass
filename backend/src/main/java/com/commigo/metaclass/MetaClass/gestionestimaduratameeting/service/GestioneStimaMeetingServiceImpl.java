package com.commigo.metaclass.MetaClass.gestionestimaduratameeting.service;

import com.commigo.metaclass.MetaClass.entity.FeedbackMeeting;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.FeedbackMeetingRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter.StimaDurataMeetingAdapter;
//import com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter.StimaDurataMeetingAdapterImpl;
import com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter.StimaDurataMeetingAdapterImpl;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("GestioneStimaMeetingService")
public class GestioneStimaMeetingServiceImpl implements GestioneStimaMeetingService {

    /**
     * Adapter per ottenere i risultati dell'agente intelligente.
     */
    private StimaDurataMeetingAdapter stimaProgressiAdapter;
    @Autowired
    private StanzaRepository stanzaRepository;
    @Autowired
    private StatoPartecipazioneRepository statoPartecipazioneRepository;
    @Autowired
    private FeedbackMeetingRepository feedbackMeetingRepository;
    private Evaluator evaluator;


    /**
     * constructor
     */
    public GestioneStimaMeetingServiceImpl() {
        stimaProgressiAdapter = new StimaDurataMeetingAdapterImpl();
    }


   /**
    *
    * @param id_stanza
    * @return
    * @throws RuntimeException403
    * @throws ServerRuntimeException
    */
    @Override
    public Double getDurataMeeting(Long id_stanza) throws RuntimeException403, ServerRuntimeException {
        //controllo la stanza se esiste
        Stanza s;
        if((s=stanzaRepository.findStanzaById(id_stanza))==null){
            throw new RuntimeException403("non è possibile effettuare la stima: stanza non trovata");
        }
        //mi recapito la lista di utenti in stanza
        List<Utente> users;
        if((users=statoPartecipazioneRepository.findUtentiInStanza(s.getId()))==null){
            throw new ServerRuntimeException("non è possibile effettuare la stima: " +
                    "errore nella ricerca dei partecipanti");
        }
        double mediaDuration = 0;

        for(Utente u : users){
            mediaDuration += requestPrediction(u);
        }
        return mediaDuration/users.size();
    }

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

    public Map<String, ?> predict(Map<FieldName, FieldValue> arguments) {
        Map<FieldName,?> results = evaluator.evaluate(arguments);

        return EvaluatorUtil.decodeAll(results);
    }


    private Double requestPrediction(Utente utente) {
        System.out.println("Applicazione avviata con successo");

        try (InputStream is = getClass().getResourceAsStream("/RegressoreDurataMeeting.pmml")) {
            evaluator = new LoadingModelEvaluatorBuilder()
                    .load(is)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //calcolo l'età
        Period periodo = Period.between(utente.getDataDiNascita(), LocalDate.now());

        //conversione sesso
        int sex;
        if(utente.getSesso().equalsIgnoreCase("M"))
            sex=2;
        else if(utente.getSesso().equalsIgnoreCase("F"))
            sex=1;
        else sex=0;

        //ricavo la media dei vari ImmersionLevel dell'utente
        List<FeedbackMeeting>feeds = feedbackMeetingRepository.findFeedbackMeetingByUtente(utente);
        double immersionLevelAverage = feeds.stream()
                .mapToInt(FeedbackMeeting::getImmersionLevel) // Estrae gli immersionLevel come IntStream
                .average() // Calcola la media degli immersionLevel
                .orElse(0.0); // Valore di default se la lista è vuota

        //ricavo la media dei motionSickness
        double motionSicknessAverage = feeds.stream()
                .mapToInt(FeedbackMeeting::getMotionSickness) // Estrae motionSickness come IntStream
                .average() // Calcola la media dei motionSickness
                .orElse(0.0); // Valore di default se la lista è vuota

        // Esempio di utilizzo
        Map<String, Object> input = new HashMap<>();
        input.put("Age",periodo.getYears());  // Sostituisci con il valore effettivo
        input.put("Gender", sex);  // Sostituisci con il valore effettivo (0"O", 1"F" o 2"M")
        input.put("ImmersionLevel", (int) immersionLevelAverage);  // Sostituisci con il valore effettivo (da 1 a 5)
        input.put("MotionSickness", (int) motionSicknessAverage);  // Sostituisci con il valore effettivo (da 1 a 10)

        Map<FieldName, FieldValue> arguments = prepareArguments(input);

        Map<String, ?> results = predict(arguments);

        // Stampa i risultati
        return (Double) results.get("Duration");
    }

    /**
     *
     * @param u
     * @param durata
     * @param immersionLevel
     * @throws ServerRuntimeException
     */
    @Override
    public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel)
            throws ServerRuntimeException {
        stimaProgressiAdapter.addUtenteInDataset(u,durata,immersionLevel);
    }
}
