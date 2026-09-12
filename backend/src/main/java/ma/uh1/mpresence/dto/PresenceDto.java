package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Une ligne de la feuille de présence d'un module (utile pour hp/create-pdf
 * côté front : Doctorant / CNE / date).
 */
@Getter
@Setter
@AllArgsConstructor
public class PresenceDto {
    private Long id;
    private String doctorantNom;
    private String doctorantPrenom;
    private String doctorantCne;
    private String doctorantCodeApogee;
    private LocalDateTime dateParticipation;
    private String recordedByNomFr;
}
