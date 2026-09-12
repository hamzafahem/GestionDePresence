package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.AdminInscriptionRequest;
import ma.uh1.mpresence.dto.InscriptionDto;
import ma.uh1.mpresence.dto.InscriptionTraitementRequest;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.InscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Vue admin/formateur des inscriptions (demandes + affectations directes)
 * des doctorants aux formations.
 */
@RestController
@RequestMapping("/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @GetMapping
    public List<InscriptionDto> getAll() {
        return inscriptionService.getAll();
    }

    @GetMapping("/formation/{formationId}")
    public List<InscriptionDto> getForFormation(@PathVariable Long formationId) {
        return inscriptionService.getForFormation(formationId);
    }

    /**
     * Affectation directe d'un doctorant à une formation par l'admin (approuvée d'office).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InscriptionDto affecterDirectement(@Valid @RequestBody AdminInscriptionRequest request,
                                               @AuthenticationPrincipal User admin) {
        return inscriptionService.affecterDirectement(request.getDoctorantId(), request.getFormationId(), admin);
    }

    @PostMapping("/{id}/traiter")
    public InscriptionDto traiter(@PathVariable Long id, @Valid @RequestBody InscriptionTraitementRequest request,
                                   @AuthenticationPrincipal User traitePar) {
        return inscriptionService.traiter(id, request.getStatut(), traitePar);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        inscriptionService.delete(id);
    }
}
