package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "annees_universitaires")
@Getter
@Setter
@NoArgsConstructor
public class AnneeUniversitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelle;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    /**
     * Une seule année universitaire doit être active à la fois : c'est celle
     * renvoyée dans la réponse de login (champ "annee_uni").
     */
    @Column(nullable = false)
    private boolean active = false;
}
