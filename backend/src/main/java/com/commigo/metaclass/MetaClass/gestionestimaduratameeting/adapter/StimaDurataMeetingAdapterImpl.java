package com.commigo.metaclass.MetaClass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.MetaClass.entity.FeedbackMeeting;
import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.repository.FeedbackMeetingRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.StatoPartecipazioneRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class StimaDurataMeetingAdapterImpl implements StimaDurataMeetingAdapter{


    /**
     * questo metodo serve per aggiungere le informazioni di un utente nel dataset
     * @param u utente di cui si vogliono aggiungere le informazioni all'interno del dataset
     * @param durata
     */

    /**
     * metodo che permette di aggiungere le informazioni di un utente nel dataset
     * @param u utente di cui si vogliono aggiungere le informazioni all'interno del dataset
     * @param durata
     * @param immersionLevel
     * @throws ServerRuntimeException
     */
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

    /**
     *
     * @param filePath
     * @return
     * @throws ServerRuntimeException
     */
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

