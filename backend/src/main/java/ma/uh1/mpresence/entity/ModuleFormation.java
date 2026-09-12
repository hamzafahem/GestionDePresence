package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Module rattaché à une formation. Nommé "ModuleFormation" pour éviter le
 * conflit avec java.lang.Module.
 */
@Entity
@Table(name = "modules_formation")
@Getter
@Setter
@NoArgsConstructor
public class ModuleFormation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    /** Formateur responsable de ce module (affectation back-office). */
    @ManyToOne
    @JoinColumn(name = "formateur_user_id")
    private User formateur;
}
