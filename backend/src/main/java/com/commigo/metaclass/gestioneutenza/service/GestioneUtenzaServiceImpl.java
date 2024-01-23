package com.commigo.metaclass.gestioneutenza.service;

import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.StatoPartecipazione;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.DataNotFoundException;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.webconfig.ValidationToken;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/** Gestione utenza service. */
@Service
@RequiredArgsConstructor
@Slf4j // serve per stampare delle cose nei log
@Transactional
// ogni operazione è una transazione
public class GestioneUtenzaServiceImpl implements GestioneUtenzaService {

  private final UtenteRepository utenteRepository;
  private final StatoPartecipazioneRepository statoPartecipazioneRepository;
  private final StanzaRepository stanzaRepository;
  @Autowired private ValidationToken validationToken;

  @Autowired private JwtTokenUtil jwtTokenUtil;
  private final Set<String> adminmetaIds = loadAdminmetaIdsFromFile();

  /**
   * Metodo che carica il metaId di un utente admin all'interno del file che contiene tutti i metaId
   * degli admin.
   *
   * @return Insieme di metaID.
   */
  private Set<String> loadAdminmetaIdsFromFile() {
    Set<String> adminIds = new HashSet<>();
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(new ClassPathResource("admins.txt").getInputStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        adminIds.add(line.trim());
      }
    } catch (IOException e) {
      // Gestione eccezioni legate alla lettura del file (ad esempio FileNotFoundException)
      e.printStackTrace();
    }
    return adminIds;
  }

  /**
   * Metodo che consente a un utente di effettuare il login/registrazione.
   *
   * @param u Utente che esegue il login/registrazione
   * @return valore boolean che identifica l'esito dell'operazione
   * @throws ServerRuntimeException Eccezione generata da un errore del server.
   */
  @Override
  public boolean loginMeta(Utente u) throws ServerRuntimeException {
    try {

      if (adminmetaIds.contains(u.getMetaId())) {
        u.setAdmin(true);
      }

      Utente ut;
      if ((ut = utenteRepository.findFirstBymetaId(u.getMetaId())) != null) {
        ut.setTokenAuth(u.getTokenAuth());
        utenteRepository.save(ut);
      } else {
        utenteRepository.save(u);
      }

      return true;
    } catch (DataIntegrityViolationException e) {
      throw new ServerRuntimeException("errore nella registrazione dell'utente");
    }
  }

  /**
   * Metodo che consente la modifica dei dati personali di un determianto utente.
   *
   * @param metaId metaId dell'utente che intende modificare i suoi dati
   * @param params nuovi dati dell'utente
   * @return valore boolean che identifica l'esito dell'operazione
   * @throws RuntimeException403 Eccezione generata da un errore del client.
   */
  @Override
  public boolean modificaDatiUtente(String metaId, Map<String, Object> params)
      throws RuntimeException403 {

    String metaId2 = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());

    Utente u = utenteRepository.findFirstBymetaId(metaId2);

    if (u == null) {
      throw new RuntimeException403("Utente non registrato nei sistemi");
    }

    return utenteRepository.updateAttributes(u.getMetaId(), params) > 0;
  }

  /**
   * Metodo che ritorna tutte le stanze di un utente.
   *
   * @param metaId metaId dell'utente
   * @return lista di stanze di un determinato utente
   * @throws ServerRuntimeException Eccezione generata da un errore del server.
   */
  @Override
  public List<Stanza> getStanzeByUserId(String metaId) throws ServerRuntimeException {
    Utente existingUser = utenteRepository.findFirstBymetaId(metaId);
    if (existingUser == null) {
      throw new ServerRuntimeException("Utente non presente nel database");
    } else {
      List<StatoPartecipazione> stati = statoPartecipazioneRepository.findAllByUtente(existingUser);
      if (stati == null) {
        throw new ServerRuntimeException("Errore nella ricerca delle stanze");
      } else {
        // Estrai gli attributi 'stanza' dalla lista 'stati' e messi in una nuova lista
        return stati.stream()
            .filter(stato -> !stato.isBannato() && !stato.isInAttesa())
            .map(StatoPartecipazione::getStanza)
            .collect(Collectors.toList());
      }
    }
  }

  /**
   * Metodo che permette di visualizzare un utente in base al suo metaId.
   *
   * @param sessionId metaId dell'utente che si desidera visualizzare
   * @return l'utente che si vuole visualizzare
   * @throws DataNotFoundException Eccezione generata quando dati nella stringa json non sono stati
   *     trovati.
   */
  @Override
  public Utente getUtenteByUserId(String sessionId) throws DataNotFoundException {
    Utente existingUser = utenteRepository.findFirstBymetaId(sessionId);
    if (existingUser == null) {
      throw new DataNotFoundException("Utente non presente nel database");
    } else {
      return existingUser;
    }
  }

  /**
   * Metodo che permette a un utente loggato di effettuare il logout.
   *
   * @param metaId metaId dell'utente che desidera effettuare il logout
   * @param validationToken token assegnato all'utente in fase di login
   * @return valore boolean che identifica la riuscita dell'operazione
   * @throws ServerRuntimeException Eccezione generata da un errore del server.
   */
  @Override
  public boolean logoutMeta(String metaId, ValidationToken validationToken)
      throws ServerRuntimeException {

    Utente u = utenteRepository.findFirstBymetaId(metaId);
    if (u == null) {
      return false;
    }

    u.setTokenAuth(Utente.DEFAULT_TOKEN);
    try {
      utenteRepository.save(u);
      return true;
    } catch (DataIntegrityViolationException e) {
      throw new ServerRuntimeException("errore nel salvataggio del utente");
    }
  }
}
