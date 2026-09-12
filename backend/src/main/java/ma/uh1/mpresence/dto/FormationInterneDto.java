package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FormationInterneDto {
    private Long id;
    private String intitule;
    private String description;
    private Long formateurId;
    private String formateurNomFr;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String lieu;
    private Integer capaciteMax;
    private int participantsCount;
}
