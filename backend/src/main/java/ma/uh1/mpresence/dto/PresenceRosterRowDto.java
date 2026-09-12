package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Une ligne de la vue "Gestion de présence" d'un module côté Formateur :
 * un doctorant de la formation, son statut de présence du jour, et une
 * éventuelle réclamation en attente pour ce module.
 */
@Getter
@Setter
@AllArgsConstructor
public class PresenceRosterRowDto {
    private Long doctorantId;
    private String nom;
    private String prenom;
    private String codeApogee;
    private String photo;
    private boolean present;
    private LocalDateTime dateParticipation;
    private Long reclamationId;
    private String reclamationType;
}
