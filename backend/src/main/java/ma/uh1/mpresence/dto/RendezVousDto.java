package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RendezVousDto {
    private Long id;
    private Long doctorantId;
    private String doctorantNom;
    private String doctorantPrenom;
    private Long formateurId;
    private String formateurNomFr;
    private String objet;
    private LocalDateTime dateHeure;
    private String lieu;
    private String statut;
    private String commentaire;
}
