package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FormationUpsertRequest {

    @NotBlank
    private String intitule;

    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long formateurId;
}
