package ma.uh1.mpresence.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest {
    private String nomFr;
    private String prenomFr;

    @Email
    private String email;

    private String photo;
    private Integer age;
    private String sexe;
    private String address;
    private String phone;
}
