package com.commigo.metaclass.MetaClass.utility.multipleid;

import com.commigo.metaclass.MetaClass.entity.Stanza;
import com.commigo.metaclass.MetaClass.entity.Utente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatoPartecipazioneId implements Serializable {

    private Utente utente;
    private Stanza stanza;

}