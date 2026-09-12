package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Demande d'un Formateur pour être affecté à une Formation entière (module
 * null) ou à un Module précis (module renseigné) — auto-service depuis
 * l'app mobile, approuvée/rejetée par l'admin depuis le back-office. Vient
 * en complément de l'affectation directe par l'admin
 * (FormationService/ModuleService.affecterFormateur), qui reste disponible.
 */
@Entity
@Table(name = "demandes_affectation_formateur")
@Getter
@Setter
@NoArgsConstructor
public class DemandeAffectationFormateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formateur_user_id", nullable = false)
    private User formateur;

    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    /** Non renseigné = demande sur la formation entière ; renseigné = demande sur ce module précis. */
    @ManyToOne
    @JoinColumn(name = "module_id")
    private ModuleFormation module;

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
