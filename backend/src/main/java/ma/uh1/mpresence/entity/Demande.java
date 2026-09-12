package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Demande soumise par un doctorant (congé, attestation, autre) — traitée par
 * un admin/formateur via le back-office.
 */
@Entity
@Table(name = "demandes")
@Getter
@Setter
@NoArgsConstructor
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    /**
     * Renseigné uniquement pour type=RECLAMATION_PRESENCE : le module pour
     * lequel le doctorant conteste une présence manquante. Son approbation
     * crée automatiquement la présence correspondante (voir DemandeService).
     */
    @ManyToOne
    @JoinColumn(name = "module_id")
    private ModuleFormation module;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String objet;

    @Column(length = 2000)
    private String description;

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
    private String reponse;
}
