package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Report {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotBlank
    @NotNull(message = "Il numero di partecipanti non può essere nullo")
    @Min(value = 1, message = "Il numero di partecipanti non può essere inferiore a 1")
    private int Num_Partecipante;



    @Column(name = "Durata_Meeting")
    @NotBlank
    @NotNull(message = "La durata del meeting non può essere nulla")
    private LocalDateTime Durata_Meeting;

    @NotBlank
    @Column(name = "MAX_Partecipanti")
    @NotNull(message = "Il numero massimo di partecipanti non può essere nullo")
    @Min(value = 1, message = "Il numero massimo di partecipanti non può essere inferiore a 1")
    private int MAX_Partecipanti;

    @NotBlank
    @NotNull(message = "Il meeting non può essere nullo")
    @ManyToOne()
    @JoinColumn(name = "id_meeting")
    private Meeting meeting;

    @NotBlank
    @OneToMany(mappedBy = "Utente")
    private List<Utente> Lista_partecipanti;

    @NotBlank
    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime Data_Creazione;


    @NotBlank
    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime Data_Aggiornamento;

}
