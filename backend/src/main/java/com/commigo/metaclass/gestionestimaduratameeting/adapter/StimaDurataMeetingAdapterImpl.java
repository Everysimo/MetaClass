package com.commigo.metaclass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

/** Adapter stima meeting. */
@Service
public class StimaDurataMeetingAdapterImpl implements StimaDurataMeetingAdapter {

  /**
   * Metodo che permette di aggiungere le informazioni di un utente nel dataset.
   *
   * @param u utente di cui si vogliono aggiungere le informazioni all'interno del dataset.
   * @param durata Durata dell'utente nel meeting.
   * @param immersionLevel Livello di quanto è stato immerso l'utente nel meeting.
   * @throws ServerRuntimeException Eccezione generata per errori del server.
   */
  @Override
  public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel)
      throws ServerRuntimeException {
    try (FileWriter fileWriter = new FileWriter("data.csv", true);
        CSVPrinter csvPrinter = CSVFormat.DEFAULT.withRecordSeparator("\n").print(fileWriter)) {

      // formatto l'età da aggiungere nel dataset
      Period periodo = Period.between(u.getDataDiNascita(), LocalDate.now());

      if (immersionLevel < 1 || immersionLevel > 5) {
        throw new ServerRuntimeException(
            "il livello di immersività deve essere un valore compreso tra 1 e 5");
      }

      // prelevo l'ultimo id del dataset
      String ultimoUserId = prelevaUltimoUserId("data.csv") + 1;

      // Aggiunta della nuova tupla di valori al CSV
      csvPrinter.printRecord(
          ultimoUserId, // UserID
          periodo.getYears(), // Age
          u.getSesso(), // Gender
          (double) durata.toMinutes(), // Duration
          immersionLevel); // ImmersionLevel

    } catch (IOException e) {
      throw new ServerRuntimeException("errore nell'apertura del dataset");
    }
  }

  /**
   * Metodo per prelevare l'ultima stringa del file 'data.cvs'.
   *
   * @param filePath path del file.
   * @return Ultimo record del file.
   * @throws ServerRuntimeException Eccezione generata per errori del server.
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
