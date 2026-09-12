package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Marque la présence d'un {@link Doctorant} à une séance d'un
 * {@link ModuleFormation}, enregistrée par un admin/formateur qui a
 * scanné/tapé le "Code Apogée" du doctorant (voir hp/qrcode côté front).
 * Un module peut avoir plusieurs séances dans le temps : une nouvelle
 * présence est créée à chaque scan, sauf si une présence existe déjà pour
 * la même journée (évite les doublons en cas de double-scan).
 */
@Entity
@Table(name = "participations")
@Getter
@Setter
@NoArgsConstructor
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleFormation module;

    /**
     * L'admin qui a scanné/validé la présence.
     */
    @ManyToOne
    @JoinColumn(name = "recorded_by_user_id")
    private User recordedBy;

    @Column(name = "code_scanned")
    private String codeScanned;

    @Column(name = "date_participation", nullable = false)
    private LocalDateTime dateParticipation = LocalDateTime.now();
}
