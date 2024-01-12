package com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.pmml4s.model.Model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
public class StimaDurataMeetingAdapterImpl implements StimaDurataMeetingAdapter {
*/
    /**
     * modello d'intelligenza artificiale.
     */
   /* private final Model modelloIntelligente;
    private StanzaRepository stanzaRepository;
    private StatoPartecipazioneRepository statoPartecipazioneRepository; */

    /**
     * constructor.
     */
   /* public StimaDurataMeetingAdapterImpl(){
        modelloIntelligente = Model.fromString("");    //da inserire specifiche del modello
    }
*/
    /**
     * metodo che serve a calcolare la durata consigliata di un meeting in base
     * al numero e tipo di persone presenti nella stanza.
     *
     * @param id_stanza identificativo stanza
     * @return durata meeting stimata
     */

    /*
    @Override
    public Double getDurataMeeting(Long id_stanza) throws RuntimeException403, ServerRuntimeException {

        //controllo la stanza se esiste
        Stanza s;
        if((s=stanzaRepository.findStanzaById(id_stanza))==null){
            throw new RuntimeException403("non è possibile effettuare la stima: stanza non trovata");
        }

        //mi recapito la lista di utenti in stanza
        List<Utente> users;
        if((users=statoPartecipazioneRepository.findUtentiInStanza(id_stanza))==null){
            throw new ServerRuntimeException("non è possibile effettuare la stima: " +
                    "errore nella ricerca dei partecipanti");
        }

        //istanzio una map che mi prenderà le informazioni dell'utente
        Map<String, Object> map = new HashMap<>();
        double mediaDuration = 0;

        for(Utente u : users){
            //mi salvo il sesso
            map.put("Gender",u.getSesso());

            //calcolo l'età dell'utente
            Period periodo = Period.between(u.getDataDiNascita(), LocalDate.now());
            map.put("Age", periodo.getYears());

            //previsione della durata stimata dell'utente u
            double duration = (double) modelloIntelligente.predict(map).get("Duration");
            mediaDuration += duration;

        }
        return mediaDuration/users.size();

    }

   */

/**
 * questo metodo serve per aggiungere le informazioni di un utente nel dataset
 *
 * @param u
 * @param durata
*/

  /*
    @Override
    public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel) throws ServerRuntimeException {
        try (FileWriter fileWriter = new FileWriter("data.csv", true);
             CSVPrinter csvPrinter = CSVFormat.DEFAULT
                     .withRecordSeparator("\n").print(fileWriter)) {

            //formatto l'età da aggiungere nel dataset
            Period periodo = Period.between(u.getDataDiNascita(), LocalDate.now());

            if(immersionLevel<1 || immersionLevel>5)
                throw new ServerRuntimeException("il livello di immersività deve essere un valore compreso tra 1 e 5");

            //prelevo l'ultimo id del dataset
            String ultimoUserId = prelevaUltimoUserId("data.csv") + 1;

            // Aggiunta della nuova tupla di valori al CSV
            csvPrinter.printRecord(ultimoUserId,                  //UserID
                                   periodo.getYears(),            //Age
                                   u.getSesso(),                  //Gender
                                   (double) durata.toMinutes(),   //Duration
                                   immersionLevel);               //ImmersionLevel

        } catch (IOException e) {
            throw new ServerRuntimeException("errore nell'apertura del dataset");
        }
    }

    private static String prelevaUltimoUserId(String filePath) throws ServerRuntimeException {
        try (FileReader fileReader = new FileReader(filePath);
             CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader)) {

            List<CSVRecord> records = csvParser.getRecords();

            if (!records.isEmpty()) {
                // Prendi l'ultima riga del CSV
                CSVRecord ultimaRiga = records.get(records.size() - 1);

                // Estrai il valore del campo "UserId"
                return ultimaRiga.get("UserID");
            } else {
                throw new ServerRuntimeException("Il dataset è vuoto.");
            }

        } catch (IOException e) {
            throw new ServerRuntimeException("Errore nell'apertura o lettura del dataset");
        }
    }


}
  */

