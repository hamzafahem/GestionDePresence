package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleUpsertRequest {

    @NotBlank
    private String nom;

    private Long formateurId;
}
