package com.commigo.metaclass.gestionestimaduratameeting.adapter;

import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.ServerRuntimeException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;


/** Adapter stima meeting. */
@Service
public class StimaDurataMeetingAdapterImpl implements StimaDurataMeetingAdapter {

  private static final String CSV_FILE_NAME = "ModuloAI/data.csv";
  private static final String PROJECT_ROOT = System.getProperty("user.dir");
  private static final String RESOURCES_PATH =
          PROJECT_ROOT + File.separator + "src" + File.separator + "main" + File.separator + "resources";

  private static int counter = 0;

  /**
   * Metodo che permette di aggiungere le informazioni di un utente nel dataset.
   *
   * @param u utente di cui si vogliono aggiungere le informazioni all'interno del dataset.
   * @param durata Durata dell'utente nel meeting.
   * @param immersionLevel Livello di quanto è stato immerso l'utente nel meeting.
   * @param motionSickness Livello di fastidio.
   * @throws ServerRuntimeException Eccezione generata per errori del server.
   */
  @Override
  public void addUtenteInDataset(Utente u, Duration durata, int immersionLevel, int motionSickness)
      throws ServerRuntimeException {

      // Ottieni il percorso del file completo
      String fullPath = RESOURCES_PATH + File.separator + CSV_FILE_NAME;

      try (FileWriter fileWriter = new FileWriter(fullPath, true);
           CSVPrinter csvPrinter = CSVFormat.DEFAULT.withRecordSeparator("\n").print(fileWriter)) {

        // formatto l'età da aggiungere nel dataset
        Period periodo = Period.between(u.getDataDiNascita(), LocalDate.now());

        if (immersionLevel < 1 || immersionLevel > 5) {
          throw new ServerRuntimeException(
              "il livello di immersività deve essere un valore compreso tra 1 e 5");
        }

        if (motionSickness < 1 || motionSickness > 10) {
          throw new ServerRuntimeException(
              "il motionSickness deve essere un valore compreso tra 1 e 10");
        }

        // prelevo l'ultimo id del dataset
        int ultimoUserId = prelevaUltimoUserId(CSV_FILE_NAME);

        // Aggiunta della nuova tupla di valori al CSV
        csvPrinter.printRecord(
            ultimoUserId + 1, // UserID
            periodo.getYears(), // Age
                (u.getSesso().equals("M") ? 2 : (u.getSesso().equals("F")  ? 1 : 0)), // Gender
            null, // VR Headset (non utilizzato e rimosso nella feature selection)
            (double) durata.toMinutes(), // Duration
            immersionLevel, // ImmersionLevel
            motionSickness); // MotionSickness

      //incremento il contatore che mi tiene traccia di quante tuple vengono aggiunte
        counter++;
      //ogni 100 inserimenti avviene il retraining
      //if ((counter% 100) == 0) {
        // ri-training del modello
        trainingModel();
     // }

      } catch (IOException e) {
        e.printStackTrace();
        throw new ServerRuntimeException("errore nell'apertura del dataset");
      }
  }

      /**
   * Metodo per prelevare l'ultima stringa del file 'data.csv'.
   *
   * @param filePath path del file.
   * @return Ultimo record del file.
   * @throws ServerRuntimeException Eccezione generata per errori del server.
   */
  private static int prelevaUltimoUserId(String filePath) throws ServerRuntimeException {
    try (Reader reader = new InputStreamReader(
            Objects.requireNonNull(StimaDurataMeetingAdapterImpl.class
                    .getClassLoader().getResourceAsStream(filePath)), StandardCharsets.UTF_8);
         CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

      List<CSVRecord> records = csvParser.getRecords();

      if (!records.isEmpty()) {
        // Prendi l'ultima riga del CSV
        CSVRecord ultimaRiga = records.get(records.size() - 1);

        // Estrai il valore del campo "UserId"
        return Integer.parseInt(ultimaRiga.get("UserID"));

      } else {
        throw new ServerRuntimeException("Il dataset è vuoto.");
      }

    } catch (IOException e) {
      e.printStackTrace();
      throw new ServerRuntimeException("Errore nell'apertura o lettura del dataset");
    } catch(NumberFormatException ne){
      ne.printStackTrace();
      throw new ServerRuntimeException("nella conversione dell'id utente nel dataset");
    }
  }

  //metodo per il riallenamento del modello
  private String trainingModel() throws ServerRuntimeException {
    String scriptPath = "trainingModel.py";
    String workingDirectory = "ModuloAI"; // Directory relativa alla posizione corrente del progetto

    try {
      ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath);
      processBuilder.directory(new File(workingDirectory)); // Imposta la directory di lavoro
      Process process = processBuilder.start();

      // Leggi lo stream di output e di errore del processo
      BufferedReader readerOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader readerError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      StringBuilder output = new StringBuilder();
      String line;

      while ((line = readerOutput.readLine()) != null) {
        output.append(line).append("\n");
      }

      StringBuilder errorOutput = new StringBuilder();
      while ((line = readerError.readLine()) != null) {
        errorOutput.append(line).append("\n");
      }

      int exitCode = process.waitFor();

      if (exitCode != 0) {
        // Se l'esecuzione ha generato un errore, restituisci sia l'output che i messaggi di errore
        throw new ServerRuntimeException("Errore nell'esecuzione del modulo Python. Messaggio di errore: " + errorOutput.toString());
      }

      // Restituisci l'output generato dal modulo Python
      return output.toString().trim();

    } catch (Exception e) {
      e.printStackTrace();
      throw new ServerRuntimeException("Errore nell'esecuzione del modulo Python");
    }
  }







}


