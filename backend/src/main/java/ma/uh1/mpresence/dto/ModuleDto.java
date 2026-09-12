package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModuleDto {
    private Long id;
    private String nom;
    private Long formationId;
    private Long formateurId;
    private String formateurNomFr;
}
