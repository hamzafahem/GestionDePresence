package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RendezVousUpsertRequest {

    @NotNull
    private Long doctorantId;

    private Long formateurId;

    @NotBlank
    private String objet;

    @NotNull
    private LocalDateTime dateHeure;

    private String lieu;

    private String statut;

    private String commentaire;
}
