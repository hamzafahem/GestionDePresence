package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InscriptionCreateRequest {

    @NotNull
    private Long formationId;
}
