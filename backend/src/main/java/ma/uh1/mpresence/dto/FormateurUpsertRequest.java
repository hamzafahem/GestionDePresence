package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormateurUpsertRequest {

    @NotBlank
    private String nomFr;

    @NotBlank
    private String prenomFr;

    @NotBlank
    private String cin;

    /** Optionnel à la modification (laisser vide = ne pas changer le mot de passe). */
    private String password;

    private String email;
    private String phone;
    private Integer age;
    private String sexe;
    private String address;
}
