package ma.uh1.mpresence.controller;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ParticipationResultDto;
import ma.uh1.mpresence.dto.PresenceDto;
import ma.uh1.mpresence.dto.PresenceRosterRowDto;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.service.ParticipationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/formations")
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;
    private final DoctorantRepository doctorantRepository;

    /**
     * Correspond à QrService.setQr() côté front :
     * GET /formations/participerModuleByQr/{idf}/{idm}?code=...
     * "code" = Code Apogée scanné/tapé sur la carte du doctorant (hp/qrcode).
     * En query param (pas en path variable) : un QR scanné par erreur peut
     * contenir des caractères comme "/" que Tomcat refuse dans un chemin
     * même encodés, ce qui casse la requête avant même d'atteindre CORS.
     * Nécessite un Bearer token valide (l'admin/formateur qui scanne).
     */
    @GetMapping("/participerModuleByQr/{idf}/{idm}")
    public ParticipationResultDto participer(
            @PathVariable Long idf,
            @PathVariable Long idm,
            @RequestParam String code,
            @AuthenticationPrincipal User currentUser
    ) {
        return participationService.participerParQr(idf, idm, code, currentUser);
    }

    /**
     * Feuille de présence d'un module — pour hp/create-pdf côté front
     * (actuellement basé sur des données hardcodées, à reconnecter ici).
     */
    @GetMapping("/{idf}/modules/{idm}/presences")
    public List<PresenceDto> presences(@PathVariable Long idf, @PathVariable Long idm) {
        return participationService.getPresences(idf, idm);
    }

    /**
     * "Gestion de présence" (Formateur) : tous les doctorants de la
     * formation, avec leur statut de présence du jour pour ce module et une
     * éventuelle réclamation en attente.
     */
    @GetMapping("/{idf}/modules/{idm}/roster")
    public List<PresenceRosterRowDto> roster(@PathVariable Long idf, @PathVariable Long idm) {
        return participationService.getRoster(idf, idm);
    }

    /** Marquage manuel d'une présence depuis la vue "Gestion de présence". */
    @PostMapping("/{idf}/modules/{idm}/doctorants/{idd}/presence")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void marquerManuellement(@PathVariable Long idf, @PathVariable Long idm, @PathVariable Long idd,
                                     @AuthenticationPrincipal User currentUser) {
        Doctorant doctorant = doctorantRepository.findById(idd)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + idd));
        participationService.marquerPresenceManuelle(idm, doctorant, currentUser);
    }

    /** Retire une présence marquée par erreur. */
    @DeleteMapping("/{idf}/modules/{idm}/doctorants/{idd}/presence")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void retirer(@PathVariable Long idf, @PathVariable Long idm, @PathVariable Long idd) {
        participationService.retirerPresence(idm, idd);
    }
}
