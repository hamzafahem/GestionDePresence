package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemandeTraitementRequest {

    @NotBlank
    private String statut;

    private String reponse;
}
