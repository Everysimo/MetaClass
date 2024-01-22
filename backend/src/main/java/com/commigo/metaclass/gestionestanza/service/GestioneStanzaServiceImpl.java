package com.commigo.metaclass.gestionestanza.service;

import com.commigo.metaclass.entity.Ruolo;
import com.commigo.metaclass.entity.Scenario;
import com.commigo.metaclass.entity.Stanza;
import com.commigo.metaclass.entity.StatoPartecipazione;
import com.commigo.metaclass.entity.Utente;
import com.commigo.metaclass.exceptions.RuntimeException401;
import com.commigo.metaclass.exceptions.RuntimeException403;
import com.commigo.metaclass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.gestioneamministrazione.repository.ScenarioRepository;
import com.commigo.metaclass.gestionestanza.repository.RuoloRepository;
import com.commigo.metaclass.gestionestanza.repository.StanzaRepository;
import com.commigo.metaclass.gestionestanza.repository.StatoPartecipazioneRepository;
import com.commigo.metaclass.gestioneutenza.repository.UtenteRepository;
import com.commigo.metaclass.utility.response.ResponseUtils;
import com.commigo.metaclass.utility.response.types.AccessResponse;
import com.commigo.metaclass.utility.response.types.Response;
import com.commigo.metaclass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.webconfig.ValidationToken;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Implementazione del service della gestione stanza. */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // ogni operazione è una transazione
public class GestioneStanzaServiceImpl implements GestioneStanzaService {

  private final StatoPartecipazioneRepository statoPartecipazioneRepository;
  private final RuoloRepository ruoloRepository;
  private final StanzaRepository stanzaRepository;
  private final UtenteRepository utenteRepository;
  private final ScenarioRepository scenarioRepository;

  @Autowired private ValidationToken validationToken;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  /**
   * metodo che permette ad un utente di accedere ad una determinata stanza.
   *
   * @param codiceStanza codice della stanza a cui l'utente vuole accedere
   * @param idUtente id dell'utente che deve accedere alla stanza
   * @return un valore long che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<AccessResponse<Long>> accessoStanza(String codiceStanza, String idUtente)
      throws ServerRuntimeException, RuntimeException403 {

    // controllo stanza se è vuota
    Stanza stanza = stanzaRepository.findStanzaByCodice(codiceStanza);
    if (stanza == null) {
      throw new RuntimeException403("stanza non trovata");
    }

    // prelevo l'utente
    Utente u = utenteRepository.findFirstBymetaId(idUtente);
    if (u == null) {
      throw new ServerRuntimeException("utente non trovato");
    }

    StatoPartecipazione sp =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);

    if (sp == null) {
      sp =
          new StatoPartecipazione(
              stanza,
              u,
              ruoloRepository.findByNome(Ruolo.PARTECIPANTE),
              !stanza.isTipoAccesso(),
              false,
              u.getNome(),
              true);
      statoPartecipazioneRepository.save(sp);

      // verifico se la stanza è privata o pubblica
      if (stanza.isTipoAccesso()) {
        return ResponseEntity.ok(
            new AccessResponse<>(stanza.getId(), "Accesso effettuato con successo", false));
      } else {
        return ResponseEntity.ok(
            new AccessResponse<>(0L, "Richiesta accesso alla stanza effettuata", true));
      }
    } else if (sp.isBannato()) {
      throw new RuntimeException403("Sei stato bannato da questa stanza, non puoi entrare");
    } else {
      return ResponseEntity.ok(
          new AccessResponse<>(stanza.getId(), "Sei già all'interno di questa stanza", false));
    }
  }

  /**
   * metodo che permette di bannare un utente generico all'interno di una specifica stanza.
   *
   * @param idStanza id della stanza da cui si vuole bannare l'utente
   * @param metaId metaId dell'utente che vuole effettuare il ban
   * @param idUtente id dell'utente che deve essere bannato
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> banUtente(Long idStanza, String metaId, Long idUtente)
      throws ServerRuntimeException, RuntimeException403 {
    if (utenteRepository.findFirstBymetaId(metaId) == null) {
      throw new ServerRuntimeException("errore nella ricerca dell'organizzatore master");
    }

    // controllo organizzatore da bannare
    Utente og;
    if ((og = utenteRepository.findUtenteById(idUtente)) == null) {
      throw new RuntimeException403("utente non trovato");
    }

    // controllo stanza
    Stanza stanza;
    if ((stanza = stanzaRepository.findStanzaById(idStanza)) == null) {
      throw new RuntimeException403("stanza non trovata");
    }

    StatoPartecipazione statoOg =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
    if (statoOg == null) {
      throw new RuntimeException403("L'utente non ha acceduto alla stanza, forse è stato kickato");
    }

    if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)) {
      return banOrganizzatore(idStanza, metaId, idUtente);
    } else if ((statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE))) {
      return banPartecipante(idStanza, metaId, idUtente);
    } else {
      return ResponseEntity.ok(new Response<>(false, "Non puoi bannare un'organizzatore master"));
    }
  }

  /**
   * metodo che permette di bannare un organizzatore all'interno di una specifica stanza.
   *
   * @param idStanza id della stanza da cui si vuole bannare l'organizzatore
   * @param metaId metaId dell'utente che vuole effettuare il ban
   * @param idUtente id dell'organizzatore che deve essere bannato
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> banOrganizzatore(
      Long idStanza, String metaId, Long idUtente)
      throws ServerRuntimeException, RuntimeException403 {
    // controllo organizzatore master
    Utente user = utenteRepository.findFirstBymetaId(metaId);
    if (user == null) {
      throw new ServerRuntimeException("errore nella ricerca dell'organizzatore master");
    }

    // controllo organizzatore da bannare
    user = utenteRepository.findUtenteById(idUtente);
    if (user == null) {
      throw new RuntimeException403("utente non trovato");
    }

    // controllo stanza
    Stanza stanza;
    if ((stanza = stanzaRepository.findStanzaById(idStanza)) == null) {
      throw new RuntimeException403("stanza non trovata");
    }

    // controllo dell'accesso dell'organizzatore master nella stanza
    StatoPartecipazione statoOgm =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(user, stanza);
    if (statoOgm == null) {
      throw new ServerRuntimeException(
          "l'organizzatore master sembra " + "non aver acceduto alla stanza");
    }

    // controllo del ruolo di organizztaore master
    if (statoOgm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)) {

      // ricerco e controllo se l'organizzatore ha fatto accesso alla stanza
      StatoPartecipazione statoOg =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(user, stanza);
      if (statoOg == null) {
        throw new RuntimeException403(
            "L'utente non ha acceduto alla stanza, forse è stato kickato");
      }

      // verifico se l'organizzatore è in attesa
      if (statoOg.isInAttesa()) {
        throw new RuntimeException403(
            "L'utente è in attesa di entrare in stanza, non può essere bannato");
      }

      // verifico se l'organizzatore è  già bannato
      if (statoOg.isBannato()) {
        throw new RuntimeException403("L'utente selezionato è già bannato!");
      }

      // se è organizzatpre allora posso bannarlo
      statoOg.setBannato(true);
      statoPartecipazioneRepository.save(statoOg);

      return ResponseEntity.ok(new Response<>(true, "L'organizzatore selezionato ora è bannato"));

    } else {
      throw new RuntimeException403(
          "Non puoi bannare un'organizzatore se non sei un organizzatore master");
    }
  }

  /**
   * metodo che permette di bannare un partecipante all'interno di una specifica stanza.
   *
   * @param idStanza id della stanza da cui si vuole bannare il partecipante
   * @param metaId metaId dell'utente che vuole effettuare il ban
   * @param idUtente id del parteciapante che deve essere bannato
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> banPartecipante(
      Long idStanza, String metaId, Long idUtente)
      throws ServerRuntimeException, RuntimeException403 {
    // controllo organizzatore master
    Utente userOgm = utenteRepository.findFirstBymetaId(metaId);
    if (userOgm == null) {
      throw new ServerRuntimeException("errore nella ricerca dell'organizzatore master");
    }

    // controllo organizzatore da bannare
    Utente user = utenteRepository.findUtenteById(idUtente);
    if (user == null) {
      throw new RuntimeException403("utente non trovato");
    }

    // controllo stanza
    Stanza stanza;
    if ((stanza = stanzaRepository.findStanzaById(idStanza)) == null) {
      throw new RuntimeException403("stanza non trovata");
    }

    // controllo dell'accesso dell'organizzatore master nella stanza
    StatoPartecipazione statoOgm =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(user, stanza);
    if (statoOgm == null) {
      throw new ServerRuntimeException(
          "l'organizzatore master sembra " + "non aver acceduto alla stanza");
    }

    // controllo del ruolo di organizztaore
    if (statoOgm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
        || statoOgm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)) {
      // ricerco e controllo se l'organizzatore ha fatto accesso alla stanza
      StatoPartecipazione statoOg =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(user, stanza);
      if (statoOg == null) {
        throw new RuntimeException403(
            "L'utente non ha acceduto alla stanza, forse è stato kickato");
      }

      // verifico se l'utente è in attesa
      if (statoOg.isInAttesa()) {
        throw new RuntimeException403(
            "L'utente è in attesa di entrare in stanza, non può essere bannato");
      }

      // verifico se l'utente è già bannato
      if (statoOg.isBannato()) {
        throw new RuntimeException403("L'utente selezionato è già bannato!");
      }

      statoOg.setBannato(true);
      statoPartecipazioneRepository.save(statoOg);
      return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è bannato"));

    } else {
      throw new RuntimeException403(
          "Non puoi bannare un'utente perché " + "non sei almeno un organizzatore");
    }
  }

  /**
   * metodo che permette la creazione di una stanza.
   *
   * @param s stanza che deve essere creata
   * @param metaId metaId dell'utente che intende creare la stanza
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public boolean creaStanza(Stanza s, String metaId) throws Exception {

    if (metaId == null) {
      throw new ServerRuntimeException("Errore col metaId");
    }

    Utente u = utenteRepository.findFirstBymetaId(metaId);
    if (u == null) {
      throw new ServerRuntimeException("Utente non trovato");
    }

    // settaggio scenario
    Scenario sc = scenarioRepository.findScenarioById(s.getScenario().getId());
    if (sc != null) {
      s.setScenario(sc);
    } else {
      throw new RuntimeException403("Scenario non trovato");
    }

    stanzaRepository.save(s);

    // Prelevo l'id della stanza a cui si deve generare il codice
    Long idStanza = stanzaRepository.findIdUltimaTupla();
    // Converto l'id in una stringa di 6 caratteri
    String codice = String.format("%06d", idStanza);
    s.setCodice(codice);
    stanzaRepository.save(s);

    StatoPartecipazione sp =
        new StatoPartecipazione(
            s,
            u,
            ruoloRepository.findByNome(Ruolo.ORGANIZZATORE_MASTER),
            false,
            false,
            u.getNome(),
            true);

    statoPartecipazioneRepository.save(sp);

    return true;
  }

  /**
   * metodo che permette di effettuare il downgrade del ruolo di un utente.
   *
   * @param idUogm id dell'utente che deve effettuare il downgrade dell'organizzatore
   * @param idog id dell'organizzatore a cui deve essere effettuato il downgrade
   * @param idStanza id della stanza su cui vuole essere il downgrade
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public Response<Boolean> downgradeUtente(String idUogm, long idog, long idStanza)
      throws ServerRuntimeException, RuntimeException403 {

    // controllo organizzatore master
    Utente userOgm = utenteRepository.findFirstBymetaId(idUogm);
    if (userOgm == null) {
      throw new ServerRuntimeException("errore nella ricerca dell'organizzatore master");
    }

    // controllo utente da promuovere
    Utente user = utenteRepository.findUtenteById(idog);
    if (user == null) {
      throw new RuntimeException403("utente non trovato");
    }

    // controllo stanza
    Stanza stanza;
    if ((stanza = stanzaRepository.findStanzaById(idStanza)) == null) {
      throw new RuntimeException403("stanza non trovata");
    }

    // controllo dell'accesso dell'organizzatore master nella stanza
    StatoPartecipazione statoOgm =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(userOgm, stanza);
    if (statoOgm == null) {
      throw new ServerRuntimeException(
          "l'organizzatore master sembra " + "non aver acceduto alla stanza");
    }

    // controllo del ruolo di organizztaore master
    if (!statoOgm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)) {
      throw new RuntimeException403(
          "Non puoi declassare un'utente perché " + "non sei un'organizzatore master");
    }

    // ricerco e controllo se l'utente ha fatto accesso alla stanza
    StatoPartecipazione statoOg =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(user, stanza);
    if (statoOg == null) {
      throw new RuntimeException403("l'utente non ha acceduto alla stanza, forse è stato kickato");
    }

    // verifico se l'utente è in attesa
    if (statoOg.isInAttesa()) {
      throw new RuntimeException403(
          "l'utente è in attesa di entrare in stanza, non può essere declassato");
    }

    // verifico se l'utente è bannato
    if (statoOg.isBannato()) {
      throw new RuntimeException403("l'utente è bannato, non può essere promosso");
    }

    // verifico il ruolo dell'utente nella stanza
    if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)) {

      // se è organizzatpre allora posso declassarlo a partecipante
      Ruolo r = ruoloRepository.findByNome(Ruolo.PARTECIPANTE);
      statoOg.setRuolo(r);
      statoPartecipazioneRepository.save(statoOg);

      return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è un partecipante"))
          .getBody();

    } else if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {
      throw new RuntimeException403("L'utente selezionato è già un partecipante");
    } else {
      throw new RuntimeException403("Sembra sia stato inviato un organizzatore master");
    }
  }

  /**
   * metodo che permette l'eliminazione di una stanza.
   *
   * @param metaId metaId dell'utente che vuole eliminare una stanza
   * @param idStanza id della stanza che deve essere eliminata
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public Response<Boolean> deleteRoom(String metaId, Long idStanza) {

    Utente ogm = utenteRepository.findFirstBymetaId(metaId);
    Stanza stanza = stanzaRepository.findStanzaById(idStanza);
    if (stanza == null) {
      return ResponseUtils.getResponseError(
              HttpStatus.INTERNAL_SERVER_ERROR, "La stanza selezionata non esiste")
          .getBody();
    }

    StatoPartecipazione statoOgm =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
    if (statoOgm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
        || ogm.isAdmin()) {
      // elimina tutti gli stati partecipazione
      statoPartecipazioneRepository.deleteAllByStanza(stanza);
      // elimina stanza
      stanzaRepository.delete(stanza);
      return ResponseEntity.ok(new Response<>(true, "Stanza eliminata con successo")).getBody();
    } else {
      return ResponseEntity.status(403)
          .body(
              new Response<>(
                  false, "Non puoi eliminare una stanza se non sei un'organizzatore master"))
          .getBody();
    }
  }

  /**
   * metodo che permette la gestione dell'accesso di un utente ad una stanza.
   *
   * @param metaId metaId dell'utente che deve gestire l'accesso alla stanza di un altro utente
   * @param idUtente id dell'utente che vuole accedere alla stanza
   * @param idStanza id della stanza acui l'utente vuole accedere
   * @param scelta valore booleano che identifica la scelta sull'accesso alla stanza da parte
   *     dell'organizzatore
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> gestioneAccesso(
      String metaId, Long idUtente, Long idStanza, boolean scelta) {

    Utente og = utenteRepository.findFirstBymetaId(metaId);
    Utente accesso = utenteRepository.findUtenteById(idUtente);
    Stanza stanza = stanzaRepository.findStanzaById(idStanza);

    if (stanza != null) {
      StatoPartecipazione statoOg =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
      if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
          || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)
              && !statoOg.isBannato()) {
        StatoPartecipazione statoAccesso =
            statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(accesso, stanza);
        if (statoAccesso.isInAttesa()) {
          if (scelta) {
            statoAccesso.setInAttesa(false);
            statoPartecipazioneRepository.save(statoAccesso);
            return ResponseEntity.ok(
                new Response<>(
                    true,
                    "L'utente selezionato non è più in attesa e sta per entrare nella stanza"));
          } else {
            statoPartecipazioneRepository.delete(statoAccesso);
            return ResponseEntity.ok(
                new Response<>(
                    true,
                    "L'utente selezionato non è più in attesa, "
                        + "hai rifiutato la richiesta di accesso alla stanza"));
          }
        } else {
          return ResponseEntity.status(403)
              .body(
                  new Response<>(
                      false, "L'utente selezioanto non è in attesa di entrare in questa stanza"));
        }
      } else {
        return ResponseEntity.status(403)
            .body(
                new Response<>(
                    false,
                    "Per accettare o rifiutare richiesta di "
                        + "accesso alla stanza devi essere almeno un organizzatore"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "La stanza selezionata non esiste"));
    }
  }

  /**
   * meotodo che permette di seleziare un parteciapante all'interno di una stanza.
   *
   * @param metaId metaId dell'utente che vuole silenziare un partecipante all'interno della stanza
   * @param idStanza id della stanza in cui si vuole silenziare un partecipante
   * @param idUtente id dell'utente che si vuole silenziare all'interno della stanza
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> silenziaPartecipante(
      String metaId, Long idStanza, Long idUtente) {

    Utente og = utenteRepository.findFirstBymetaId(metaId);
    Utente silenzia = utenteRepository.findUtenteById(idUtente);
    Stanza stanza = stanzaRepository.findStanzaById(idStanza);

    if (stanza != null) {
      StatoPartecipazione statoOg =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
      if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
          || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)
              && !statoOg.isBannato()) {
        StatoPartecipazione statoSilenzio =
            statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(
                silenzia, stanza);
        if (statoSilenzio != null) {
          if (!statoSilenzio.isSilenziato()) {
            statoSilenzio.setSilenziato(true);
            statoPartecipazioneRepository.save(statoSilenzio);
            return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è silenziato"));
          } else {
            return ResponseEntity.ok(new Response<>(true, "L'utente selezionato è gia silenziato"));
          }
        } else {
          return ResponseEntity.status(403)
              .body(new Response<>(false, "L'utente selezioanto non è presente nella stanza"));
        }
      } else {
        return ResponseEntity.status(403)
            .body(
                new Response<>(
                    false,
                    "Per silenziare un partecipante nella stanza "
                        + "devi essere almeno un organizzatore"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "La stanza selezionata non esiste"));
    }
  }

  /**
   * metodo che consente la modifica dei dati della stanza.
   *
   * @param params nuovi dati della stanza
   * @param id id della stanza da modificare
   */
  @Override
  public Boolean modificaDatiStanza(Map<String, Object> params, Long id)
      throws RuntimeException403, RuntimeException401 {

    // controllo del ruolo di ogm
    String metaId = jwtTokenUtil.getmetaIdFromToken(validationToken.getToken());
    Utente ogm = utenteRepository.findFirstBymetaId(metaId);
    Stanza existingStanza = stanzaRepository.findStanzaById(id);

    if (existingStanza == null) {
      throw new RuntimeException403("La stanza non esiste");
    }

    StatoPartecipazione statoutente =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, existingStanza);

    if (statoutente == null) {
      throw new RuntimeException403("Non hai acceduto alla stanza");
    }

    if (!statoutente.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {
      return stanzaRepository.updateAttributes(id, params) > 0;
    } else {
      throw new RuntimeException401("devi essere almeno un organizzatore");
    }
  }

  /**
   * metodo che permette la visualizzazione di lo scenario in uso all'interno di una stanza.
   *
   * @param id id della stanza di cui si vuole visualizzare lo scenario in uso
   * @return scenario in uso in una stanza ed un messaggio che descrive l'esito dell'operazione
   */
  @Override
  public ResponseEntity<Response<Scenario>> findStanza(Long id) {
    Stanza stanza = stanzaRepository.findStanzaById(id);

    if (stanza == null) {
      return ResponseEntity.status(500)
          .body(new Response<>(null, "La stanza selezionata non esiste"));
    } else {

      return ResponseEntity.ok(
          new Response<>(stanza.getScenario(), "operazione effettuata con successo"));
    }
  }

  /**
   * metodo che permette di effettuare l'upgrade di un partecipante in organizzatore all'interno di
   * una stanza.
   *
   * @param idUogm id dell'utente che vuole effettuare l'upgrade
   * @param idog id dell'utente su cui verrà effettuato l'upgradde
   * @param idStanza id della stanza in cui l'utente diventerà organizzatore
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public Response<Boolean> upgradeUtente(String idUogm, long idog, long idStanza)
      throws ServerRuntimeException, RuntimeException403 {

    // controllo organizzatore master
    Utente ogm;
    if ((ogm = utenteRepository.findFirstBymetaId(idUogm)) == null) {
      throw new ServerRuntimeException("errore nella ricerca dell'organizzatore master");
    }

    // controllo utente da promuovere
    Utente og;
    if ((og = utenteRepository.findUtenteById(idog)) == null) {
      throw new RuntimeException403("utente non trovato");
    }

    // controllo stanza
    Stanza stanza;
    if ((stanza = stanzaRepository.findStanzaById(idStanza)) == null) {
      throw new RuntimeException403("stanza non trovata");
    }

    // controllo dell'accesso dell'organizzatore master nella stanza
    StatoPartecipazione statoOgm =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(ogm, stanza);
    if (statoOgm == null) {
      throw new ServerRuntimeException(
          "l'organizzatore master sembra " + "non aver acceduto alla stanza");
    }

    // controllo del ruolo di organizztaore master
    if (!statoOgm.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)) {
      throw new RuntimeException403(
          "Non puoi promuovere un'utente perché " + "non sei un'organizzatore master");
    }

    // ricerco e controllo se l'utente ha fatto accesso alla stanza
    StatoPartecipazione statoOg =
        statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
    if (statoOg == null) {
      throw new RuntimeException403("l'utente non ha acceduto alla stanza, magari è stato kickato");
    }

    // verifico se l'utente è in attesa
    if (statoOg.isInAttesa()) {
      throw new RuntimeException403(
          "l'utente è in attesa di entrare in stanza, non può essere promosso");
    }

    // verifico se l'utente è bannato
    if (statoOg.isBannato()) {
      throw new RuntimeException403("l'utente è bannato, non può essere promosso");
    }

    // verifico il ruolo dell'utente nella stanza
    if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {

      // se è partecipante allora posso promuoverlo ad organizzatore
      Ruolo r = ruoloRepository.findByNome(Ruolo.ORGANIZZATORE);
      statoOg.setRuolo(r);
      statoPartecipazioneRepository.save(statoOg);

      return ResponseEntity.ok(new Response<>(true, "L'utente selezionato ora è un organizzatore"))
          .getBody();

    } else if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)) {
      throw new RuntimeException403("L'utente selezionato è già un'organizzatore");
    } else {
      throw new RuntimeException403("Sembra sia stato inviato un organizzatore master");
    }
  }

  /**
   * metodo che permette la visualizzazione di tutti gli utenti all'interno di una specifica stanza.
   *
   * @param id id della stanza di cui vogliamo visualizzare gli utenti al suo interno
   * @return lista di utenti all'interno della stanza specificata ed un messaggio che descrive
   *     l'esito dell'operazione
   */
  @Override
  public ResponseEntity<Response<List<Utente>>> visualizzaUtentiInStanza(Long id) {

    Stanza stanza = stanzaRepository.findStanzaById(id);

    if (stanza != null) {
      List<Utente> utenti = statoPartecipazioneRepository.findUtentiInStanza(id);
      if (utenti != null) {
        return ResponseEntity.ok(new Response<>(utenti, "operazione effettuata con successo"));
      } else {
        return ResponseEntity.ok(
            new Response<>(null, "Non sono presenti utenti all'interno della stanza"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "La stanza selezionata non esiste"));
    }
  }

  /**
   * metodo che permette la visualizzazione degli utenti bannatiall'interno di una stanza.
   *
   * @param id id della stanza di cui si vogliono visualizzare gli utenti bannati
   * @return lista di utenti bannati all'interno della stanza selezionata ed un messaggio che
   *     descrive l'esito dell'operazione
   */
  @Override
  public ResponseEntity<Response<List<Utente>>> visualizzaUtentiBannatiInStanza(Long id) {

    Stanza stanza = stanzaRepository.findStanzaById(id);

    if (stanza != null) {
      List<Utente> utenti = statoPartecipazioneRepository.findUtentiBannatiInStanza(id);
      if (utenti != null) {
        return ResponseEntity.ok(new Response<>(utenti, "operazione effettuata con successo"));
      } else {
        return ResponseEntity.ok(
            new Response<>(null, "Non sono presenti utenti bannati all'interno della stanza"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "La stanza selezionata non esiste"));
    }
  }

  /**
   * metodo che permette la visualizzazione degli utenti in attesa all'interno di una stanza.
   *
   * @param id id della stanza di cui vogliamo visualizzare gli utenti in attesa
   * @param metaId metaId dell'utente che vuole visualizzare gli utenti in attesa
   * @return lista di utenti in attesa all'interno di una stanza ed un messaggio che descrive
   *     l'esito dell'operazione
   */
  @Override
  public ResponseEntity<Response<List<Utente>>> visualizzaUtentiInAttesaInStanza(
      Long id, String metaId) {
    Stanza stanza = stanzaRepository.findStanzaById(id);
    Utente u = utenteRepository.findFirstBymetaId(metaId);
    if (stanza != null) {
      StatoPartecipazione stato =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);
      if (stato != null) {
        if (stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
            || stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)
                && !stato.isBannato()) {
          List<Utente> utenti = statoPartecipazioneRepository.findUtentiInAttesaInStanza(id);

          if (utenti != null) {
            return ResponseEntity.ok(new Response<>(utenti, "operazione effettuata con successo"));
          } else {
            return ResponseEntity.ok(
                new Response<>(
                    null, "Non sono presenti utenti in attesa all'interno della stanza"));
          }
        } else {
          return ResponseEntity.status(403)
              .body(
                  new Response<>(
                      null,
                      "Non puoi visualizzare gli utenti"
                          + " in attesa se non sei almeno un organizzatore"));
        }
      } else {
        return ResponseEntity.status(403)
            .body(
                new Response<>(
                    null,
                    "Non puoi visualizzare gli utenti in attesa"
                        + " della stanza se non sei almeno un'organizzatore di essa"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(null, "La stanza selezionata non esiste"));
    }
  }

  /**
   * metodo che permette la modifica dellos cenario in uso all'itnerno di una stanza.
   *
   * @param metaId metaId dell'utente che vuole effettuare la modifica dello scenario all'interno
   *     della stanza
   * @param idScenario id del nuovo scenario
   * @param idStanza id della stanza
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> modificaScenario(
      String metaId, Long idScenario, Long idStanza) {
    Utente u = utenteRepository.findFirstBymetaId(metaId);
    Stanza stanza = stanzaRepository.findStanzaById(idStanza);

    if (stanza != null) {
      StatoPartecipazione stato =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza);
      if (stato != null) {
        if (stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
            || stato.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)
                && !stato.isBannato()) {
          Scenario scenario = scenarioRepository.findScenarioById(idScenario);
          if (scenario != null) {
            if (scenario != stanza.getScenario()) {
              stanza.setScenario(scenario);
              stanzaRepository.save(stanza);
              return ResponseEntity.ok(new Response<>(true, "Lo scenario è stato modificato"));
            } else {
              return ResponseEntity.status(403)
                  .body(
                      new Response<>(false, "Lo scenario selezionato è già in uso per la stanza"));
            }
          } else {
            return ResponseEntity.status(403)
                .body(new Response<>(false, "Lo scenario selezionato non esiste"));
          }
        } else {
          return ResponseEntity.status(403)
              .body(
                  new Response<>(
                      false,
                      "Non puoi modificare lo " + "scenario se non sei almeno un organizzatore"));
        }
      } else {
        return ResponseEntity.status(403)
            .body(
                new Response<>(
                    false,
                    "Non sei all'interno della stanza, " + "non puoi modificare lo scenario"));
      }

    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "La stanza selezionata non esiste"));
    }
  }

  /**
   * Metodo che permette la modifica del nome all'interno di una stanza di uno specifico
   * partecipante.
   *
   * @param metaId metaId dell'utente che vuole effettuare la modifica del nome
   * @param idStanza id della stanza in cui si vuole modificare il nome
   * @param idUtente id dell'utente a cui viene modificato il nome all'itnerno della stanza
   * @param nome nuovo nome dell'utente
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> modificaNomePartecipante(
      String metaId, Long idStanza, Long idUtente, String nome) {
    Utente og = utenteRepository.findFirstBymetaId(metaId);
    Stanza stanza = stanzaRepository.findStanzaById(idStanza);

    Utente modifica = utenteRepository.findUtenteById(idUtente);
    if (stanza != null) {
      StatoPartecipazione statoOg =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
      if (statoOg != null) {
        if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
            || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)
                && !statoOg.isBannato()) {

          StatoPartecipazione statoModifica =
              statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(
                  modifica, stanza);
          if (statoModifica != null) {
            statoModifica.setNomeInStanza(nome);
            statoPartecipazioneRepository.save(statoModifica);

            return ResponseEntity.ok(new Response<>(true, "Il nome in stanza è stato modificato"));
          } else {
            return ResponseEntity.status(403)
                .body(
                    new Response<>(
                        false, "Il partecipante selezionato non è presente nella stanza"));
          }
        } else {
          return ResponseEntity.status(403)
              .body(
                  new Response<>(
                      false,
                      "Non puoi modificare il nome in stanza di un"
                          + " utente se non sei almeno un'organizzatore"));
        }
      } else {
        return ResponseEntity.status(403)
            .body(
                new Response<>(
                    false,
                    "Non puoi modificare il nome in stanza "
                        + "dell'utente selezionato perché non è presente in stanza"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "La stanza selezionata non esiste"));
    }
  }

  /**
   * metodo che permette di kickare un partecipante da una stanza.
   *
   * @param metaId metaId dell'utente che vuole effettuare il kick del partecipante
   * @param idStanza id della stanza dal quale deve essere kickato l'utente
   * @param idUtente id dell'utente che deve essere keckato
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  @Override
  public ResponseEntity<Response<Boolean>> kickPartecipante(
      String metaId, Long idStanza, Long idUtente) {
    Utente og = utenteRepository.findFirstBymetaId(metaId);
    Stanza stanza = stanzaRepository.findStanzaById(idStanza);

    Utente kick = utenteRepository.findUtenteById(idUtente);
    if (stanza != null) {
      StatoPartecipazione statoOg =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
      if (statoOg != null) {
        if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
            || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)
                && !statoOg.isBannato()) {

          StatoPartecipazione statoKick =
              statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(kick, stanza);
          if (statoKick != null) {
            statoPartecipazioneRepository.delete(statoKick);
            return ResponseEntity.ok(new Response<>(true, "L'utente è stato kickato con successo"));
          } else {
            return ResponseEntity.status(403)
                .body(
                    new Response<>(
                        false, "Il partecipante selezionato non è presente nella stanza"));
          }
        } else {
          return ResponseEntity.status(403)
              .body(
                  new Response<>(
                      false, "Non puoi kickare un utente se non sei almeno un'organizzatore"));
        }
      } else {
        return ResponseEntity.status(403)
            .body(new Response<>(false, "Il partecipante selezionato non è presente nella stanza"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "La stanza selezionata non esiste"));
    }
  }

  /**
   * meotodo che permette di visualizzare il ruolo dell'utente all'interno di una stanza.
   *
   * @param metaId metaId dell'utente che vuole visualizzare il proprio ruolo
   * @param idStanza id della stanza
   * @return il ruolo del'utente all'interno della stanza
   */
  @Override
  public Ruolo getRuoloByUserAndStanzaId(String metaId, Long idStanza)
      throws ServerRuntimeException, RuntimeException403 {

    Utente u;
    Stanza stanza;
    if ((u = utenteRepository.findFirstBymetaId(metaId)) == null) {
      throw new ServerRuntimeException(("Utente non torvato"));
    }
    if ((stanza = stanzaRepository.findStanzaById(idStanza)) == null) {
      throw new RuntimeException403("Stanza non trovata");
    }

    StatoPartecipazione sp;
    if ((sp = statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(u, stanza))
        == null) {
      throw new RuntimeException403("L'utente non ha acceduto alla stanza");
    }

    if (sp.getRuolo().getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)) {
      if (sp.isBannato()) {
        throw new RuntimeException403("Utente bannato dalla stanza");
      }
      if (sp.isInAttesa()) {
        throw new RuntimeException403("Utente in attesa di entrare in stanza");
      }
    }

    return sp.getRuolo();
  }

  /**
   * metodo che consente di visualizzare una determinata stanza.
   *
   * @param id id della stanza da visualizzare
   * @return la stanza che si vuole visualizzare
   */
  @Override
  public Stanza visualizzaStanza(Long id) {
    return stanzaRepository.findStanzaById(id);
  }

  /**
   * metodo che consente di visualizzare tutti gli scenari.
   *
   * @return lista di scenari presenti sul DB
   */
  public List<Scenario> getAllScenari() {
    return scenarioRepository.findAll();
  }

  /**
   * meotodo che consente ad un organizzatore di smutare un partecipante all'interno di una stanza.
   *
   * @param metaId metaId dell'utente che desidera effettuare l'operazione
   * @param idStanza id della stanza su cui si vuole effettuare l'oeprazione
   * @param idUtente id dell'utente per cui si vuole effettuare l'operazione
   * @return valore boolean che identifica la riuscita dell'operazione ed un messaggio che descrive
   *     l'esito di essa
   */
  public ResponseEntity<Response<Boolean>> unmutePartecipante(
      String metaId, Long idStanza, Long idUtente) {

    Utente og = utenteRepository.findFirstBymetaId(metaId);
    Utente silenzia = utenteRepository.findUtenteById(idUtente);
    Stanza stanza = stanzaRepository.findStanzaById(idStanza);

    if (stanza != null) {
      StatoPartecipazione statoOg =
          statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(og, stanza);
      if (statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER)
          || statoOg.getRuolo().getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE)
              && !statoOg.isBannato()) {
        StatoPartecipazione statoSilenzio =
            statoPartecipazioneRepository.findStatoPartecipazioneByUtenteAndStanza(
                silenzia, stanza);
        if (statoSilenzio != null) {
          if (statoSilenzio.isSilenziato()) {
            statoSilenzio.setSilenziato(false);
            statoPartecipazioneRepository.save(statoSilenzio);
            return ResponseEntity.ok(
                new Response<>(true, "L'utente selezionato ora non è più silenziato"));
          } else {
            return ResponseEntity.ok(new Response<>(true, "L'utente selezionato è gia mutato"));
          }
        } else {
          return ResponseEntity.status(403)
              .body(new Response<>(false, "L'utente selezioanto " + "non è presente nella stanza"));
        }
      } else {
        return ResponseEntity.status(403)
            .body(
                new Response<>(
                    false,
                    "Per silenziare un partecipante nella stanza "
                        + "devi essere almeno un organizzatore"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new Response<>(false, "La stanza selezionata non esiste"));
    }
  }
}
