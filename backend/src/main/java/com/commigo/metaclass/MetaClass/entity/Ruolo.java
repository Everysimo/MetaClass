package com.commigo.metaclass.MetaClass.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruolo {

    public static final int MAX_ROLE_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_ROLE_NAME_LENGTH)
    @NotBlank
    private String Nome;

    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime Data_Creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime Data_Aggiornamento;

}
