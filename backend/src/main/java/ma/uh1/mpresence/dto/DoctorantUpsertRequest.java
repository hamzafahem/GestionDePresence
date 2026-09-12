package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DoctorantUpsertRequest {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    private String email;
    private String mobile;
    private String cin;
    private String cne;

    @NotBlank
    private String codeApogee;

    private String filiere;
    private String nationalite;
    private String photo;
    private LocalDate dateNaissance;
    private String details;
    private Long formationId;
    private String statut;
}
