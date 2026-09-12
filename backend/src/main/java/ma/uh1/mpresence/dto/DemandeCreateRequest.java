package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemandeCreateRequest {

    @NotNull
    private Long doctorantId;

    @NotBlank
    private String type;

    @NotBlank
    private String objet;

    private String description;

    /** Renseigné uniquement pour type=RECLAMATION_PRESENCE. */
    private Long moduleId;
}
