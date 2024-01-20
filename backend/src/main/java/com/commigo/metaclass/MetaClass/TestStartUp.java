package com.commigo.metaclass.MetaClass;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.pmml4s.model.Model;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class TestStartUp implements ApplicationListener<ApplicationReadyEvent> {

  private Evaluator evaluator;

  private Model modelloIntelligente;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
      System.out.println("Applicazione avviata con successo");

      try (InputStream is = getClass().getResourceAsStream("/RegressoreDurataMeeting.pmml")) {
        evaluator = new LoadingModelEvaluatorBuilder()
                .load(is)
                .build();
      } catch (Exception e) {
        e.printStackTrace();
      }

      // Esempio di utilizzo
      Map<String, Object> input = new HashMap<>();
      input.put("Age", 60);  // Sostituisci con il valore effettivo
      input.put("Gender", 1);  // Sostituisci con il valore effettivo (0"O", 1"F" o 2"M")
      input.put("ImmersionLevel", 1);  // Sostituisci con il valore effettivo (da 1 a 5)
      input.put("MotionSickness", 1);  // Sostituisci con il valore effettivo (da 1 a 10)

      Map<FieldName, FieldValue> arguments = prepareArguments(input);

      Map<String, ?> results = predict(arguments);

      // Stampa i risultati
      System.out.println("Predicted Meeting Duration: " + results.get("Duration"));
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

  @Override
  public boolean supportsAsyncExecution() {
    return ApplicationListener.super.supportsAsyncExecution();
  }
}
