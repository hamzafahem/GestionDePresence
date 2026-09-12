package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DemandeAffectationFormateurDto {
    private Long id;
    private Long formateurId;
    private String formateurNomFr;
    private String formateurPrenomFr;
    private Long formationId;
    private String formationIntitule;
    private Long moduleId;
    private String moduleNom;
    private String statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateTraitement;
    private String traiteParNomFr;
    private String reponse;
}
