package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Correspond à src/app/page/page/page/profil (actuellement 100% hardcodé
 * côté front, à connecter à GET/PUT /users/me).
 */
@Getter
@Setter
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String cin;
    private String nomFr;
    private String prenomFr;
    private String email;
    private String photo;
    private Integer age;
    private String sexe;
    private String address;
    private String phone;
}
