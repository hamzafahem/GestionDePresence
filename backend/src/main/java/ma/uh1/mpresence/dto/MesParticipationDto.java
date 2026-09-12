package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Une formation à laquelle le doctorant connecté a déjà participé (vue
 * "Accueil" de l'app mobile côté Doctorant).
 */
@Getter
@Setter
@AllArgsConstructor
public class MesParticipationDto {
    private Long formationId;
    private String formationIntitule;
    private Long moduleId;
    private String moduleNom;
    private LocalDateTime dateParticipation;
}
