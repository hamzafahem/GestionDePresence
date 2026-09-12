package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InscriptionDto {
    private Long id;
    private Long doctorantId;
    private String doctorantNom;
    private String doctorantPrenom;
    private Long formationId;
    private String formationIntitule;
    private String statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateTraitement;
    private String traiteParNomFr;
}
