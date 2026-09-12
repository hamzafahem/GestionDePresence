package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Réinscription annuelle d'un doctorant à une année universitaire donnée.
 */
@Entity
@Table(name = "reinscriptions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctorant_id", "annee_universitaire_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class Reinscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    @ManyToOne
    @JoinColumn(name = "annee_universitaire_id", nullable = false)
    private AnneeUniversitaire anneeUniversitaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutWorkflow statut = StatutWorkflow.EN_ATTENTE;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @ManyToOne
    @JoinColumn(name = "traite_par_user_id")
    private User traitePar;

    @Column(length = 2000)
    private String commentaire;
}
