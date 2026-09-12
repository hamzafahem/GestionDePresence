package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AnneeUniversitaireDto {
    private Long id;
    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
