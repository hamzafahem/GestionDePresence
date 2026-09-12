package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Travail de recherche d'un doctorant : Article, Proceeding (conférence) ou
 * Brevet. Regroupés dans une seule entité (mêmes champs) plutôt que trois
 * entités quasi identiques ; "reference" porte le nom de la revue, de la
 * conférence ou le numéro de dépôt selon le type.
 */
@Entity
@Table(name = "publications_recherche")
@Getter
@Setter
@NoArgsConstructor
public class PublicationRecherche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePublication type;

    @Column(nullable = false)
    private String titre;

    /**
     * Revue (ARTICLE), conférence/lieu (PROCEEDING) ou numéro de dépôt (BREVET).
     */
    private String reference;

    @Column(name = "date_publication")
    private LocalDate datePublication;

    private String lien;

    @Column(length = 2000)
    private String description;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
}
