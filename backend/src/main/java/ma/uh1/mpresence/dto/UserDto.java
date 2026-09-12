package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String nomFr;
    private String prenomFr;
    private String email;
    private String photo;
    private List<String> roles;
    private List<String> permissions;
}
