package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private UserDto user;
    private AnneeUniversitaireDto anneeUni;
}
