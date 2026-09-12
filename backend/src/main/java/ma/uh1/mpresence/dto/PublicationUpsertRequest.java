package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PublicationUpsertRequest {

    @NotNull
    private Long doctorantId;

    @NotBlank
    private String type;

    @NotBlank
    private String titre;

    private String reference;
    private LocalDate datePublication;
    private String lien;
    private String description;
}
