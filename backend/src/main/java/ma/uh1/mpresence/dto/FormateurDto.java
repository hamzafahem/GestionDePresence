package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FormateurDto {
    private Long id;
    private String cin;
    private String nomFr;
    private String prenomFr;
    private String email;
    private String phone;
    private Integer age;
    private String sexe;
    private String address;
    private String photo;
    private boolean enabled;
    private long nbFormationsAffectees;
    private long nbModulesAffectes;
}
