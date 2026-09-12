package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Formation/atelier interne au CEDoc (distincte de {@link Formation}, le
 * programme doctoral lui-même) : ex. "Rédaction scientifique". Les
 * doctorants s'y inscrivent.
 */
@Entity
@Table(name = "formations_internes")
@Getter
@Setter
@NoArgsConstructor
public class FormationInterne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String intitule;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "formateur_user_id")
    private User formateur;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String lieu;

    @Column(name = "capacite_max")
    private Integer capaciteMax;

    @ManyToMany
    @JoinTable(
            name = "formation_interne_participants",
            joinColumns = @JoinColumn(name = "formation_interne_id"),
            inverseJoinColumns = @JoinColumn(name = "doctorant_id")
    )
    private Set<Doctorant> participants = new HashSet<>();
}
