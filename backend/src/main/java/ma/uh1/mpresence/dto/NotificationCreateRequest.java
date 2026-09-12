package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCreateRequest {

    @NotBlank
    private String titre;

    @NotBlank
    private String message;

    /**
     * null = notification diffusée à tous les doctorants.
     */
    private Long doctorantId;
}
