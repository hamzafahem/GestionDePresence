package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.FormationService;
import ma.uh1.mpresence.service.InscriptionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/formations/{formationId}/inscriptions")
@RequiredArgsConstructor
public class InscriptionWebController {

    private final InscriptionService inscriptionService;
    private final DoctorantService doctorantService;
    private final FormationService formationService;

    @GetMapping
    public String list(@PathVariable Long formationId, Model model) {
        model.addAttribute("formation", formationService.getOne(formationId));
        model.addAttribute("inscriptions", inscriptionService.getForFormation(formationId));
        model.addAttribute("doctorants", doctorantService.getAll());
        return "backoffice/formations/inscriptions";
    }

    @PostMapping
    public String affecterDirectement(@PathVariable Long formationId, @RequestParam Long doctorantId,
                                       @AuthenticationPrincipal User admin) {
        inscriptionService.affecterDirectement(doctorantId, formationId, admin);
        return "redirect:/backoffice/formations/" + formationId + "/inscriptions";
    }

    @PostMapping("/{id}/traiter")
    public String traiter(@PathVariable Long formationId, @PathVariable Long id,
                           @RequestParam String statut, @AuthenticationPrincipal User admin) {
        inscriptionService.traiter(id, statut, admin);
        return "redirect:/backoffice/formations/" + formationId + "/inscriptions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long formationId, @PathVariable Long id) {
        inscriptionService.delete(id);
        return "redirect:/backoffice/formations/" + formationId + "/inscriptions";
    }
}
