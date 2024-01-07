package com.commigo.metaclass.MetaClass.inizializer;
import com.commigo.metaclass.MetaClass.entity.Ruolo;
import com.commigo.metaclass.MetaClass.gestionestanza.repository.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RuoloRepository ruoloRepository;

    @Autowired
    public DataInitializer(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!ruoloRepository.existsRuoloByNome("Partecipante")) {
            ruoloRepository.save(new Ruolo("Partecipante"));
        }

        if (!ruoloRepository.existsRuoloByNome("Organizzatore")) {
            ruoloRepository.save(new Ruolo("Organizzatore"));
        }

        if (!ruoloRepository.existsRuoloByNome("Organizzatore_Master")) {
            ruoloRepository.save(new Ruolo("Organizzatore_Master"));
        }
    }

}
