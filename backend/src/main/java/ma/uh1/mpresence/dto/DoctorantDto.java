package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DoctorantDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String mobile;
    private String cin;
    private String cne;
    private String codeApogee;
    private String filiere;
    private String nationalite;
    private String photo;
    private LocalDate dateNaissance;
    private String details;
    private Long formationId;
    private String formationIntitule;
    private String anneeUniversitaireLibelle;
    private String statut;
    private Long encadrantId;
    private String encadrantNomFr;
    private java.time.LocalDateTime dateCreation;
}
