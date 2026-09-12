package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.InscriptionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Vue globale de toutes les demandes d'inscription (toutes formations
 * confondues), pour la découvrabilité côté admin — en complément de la vue
 * imbriquée par formation (/backoffice/formations/{id}/inscriptions), qui
 * reste disponible pour l'affectation directe d'un doctorant.
 */
@Controller
@RequestMapping("/backoffice/inscriptions")
@RequiredArgsConstructor
public class InscriptionsGlobalWebController {

    private final InscriptionService inscriptionService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inscriptions", inscriptionService.getAll());
        return "backoffice/inscriptions/list";
    }

    @PostMapping("/{id}/traiter")
    public String traiter(@PathVariable Long id, @RequestParam String statut,
                           @AuthenticationPrincipal User admin) {
        inscriptionService.traiter(id, statut, admin);
        return "redirect:/backoffice/inscriptions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        inscriptionService.delete(id);
        return "redirect:/backoffice/inscriptions";
    }
}
