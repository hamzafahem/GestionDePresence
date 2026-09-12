package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FormationInterneUpsertRequest {

    @NotBlank
    private String intitule;

    private String description;
    private Long formateurId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String lieu;
    private Integer capaciteMax;
}
