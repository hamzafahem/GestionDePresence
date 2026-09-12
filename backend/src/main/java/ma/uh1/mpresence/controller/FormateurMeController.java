package ma.uh1.mpresence.controller;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DemandeAffectationFormateurDto;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.AffectationFormateurService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Auto-service du Formateur connecté sur l'app mobile : ses demandes
 * d'affectation à une Formation entière ou à un Module précis (en
 * complément de l'affectation directe par l'admin depuis le back-office).
 * La liste des formations/modules à parcourir reste GET /administration/formations
 * et GET /administration/formations/{id}/modules, déjà accessibles à tout
 * utilisateur authentifié et qui exposent déjà formateurId/formateurNomFr.
 */
@RestController
@RequestMapping("/formateurs/me")
@RequiredArgsConstructor
public class FormateurMeController {

    private final AffectationFormateurService affectationFormateurService;

    @GetMapping("/demandes")
    public List<DemandeAffectationFormateurDto> mesDemandes(@AuthenticationPrincipal User formateur) {
        return affectationFormateurService.getMine(formateur.getId());
    }

    @PostMapping("/demandes/formation")
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeAffectationFormateurDto demanderFormation(@AuthenticationPrincipal User formateur,
                                                              @RequestParam Long formationId) {
        return affectationFormateurService.demanderFormation(formateur, formationId);
    }

    @PostMapping("/demandes/module")
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeAffectationFormateurDto demanderModule(@AuthenticationPrincipal User formateur,
                                                           @RequestParam Long moduleId) {
        return affectationFormateurService.demanderModule(formateur, moduleId);
    }

    @DeleteMapping("/demandes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annuler(@AuthenticationPrincipal User formateur, @PathVariable Long id) {
        affectationFormateurService.annuler(formateur, id);
    }
}
