package com.commigo.metaclass.MetaClass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    
    public static final int MAX_NAME_LENGTH = 254;
    public static final int MIN_NAME_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(unique = true)
    @Size(min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Lunghezza nome errata")
    @Pattern(regexp = "^[A-Z][a-z]*",
            message = "Formato nome errato")
    @NotBlank
    private String nome;


    @NotNull(message = "La descrizione non può essere nulla")
    @Column
    @Size(min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Lunghezza descrizione errata")
    @Pattern(regexp="^[a-zA-Z0-9.,!?()'\"\\-\\s]+$",
            message = "Formato descrizione errato")
    @NotBlank
    private String descrizione_categoria;
}
