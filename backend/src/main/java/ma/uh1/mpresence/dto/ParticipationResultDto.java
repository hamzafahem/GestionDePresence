package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParticipationResultDto {
    private boolean success;
    private String message;
    private String doctorantNom;
    private String doctorantPrenom;
    private Long doctorantId;
    /** true si une présence existait déjà aujourd'hui (aucune nouvelle présence créée). */
    private boolean dejaEnregistre;
}
