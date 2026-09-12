package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Demande d'inscription d'un doctorant à une Formation (auto-service depuis
 * l'app mobile, ou affectation directe par l'admin depuis le back-office —
 * voir {@link StatutWorkflow}). Une fois APPROUVEE, met à jour
 * {@link Doctorant#getFormation()}.
 */
@Entity
@Table(name = "inscriptions")
@Getter
@Setter
@NoArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

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
}
