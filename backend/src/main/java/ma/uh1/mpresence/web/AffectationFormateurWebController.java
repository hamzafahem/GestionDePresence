package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.AffectationFormateurService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/affectations-formateurs")
@RequiredArgsConstructor
public class AffectationFormateurWebController {

    private final AffectationFormateurService affectationFormateurService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("demandes", affectationFormateurService.getAll());
        return "backoffice/affectations-formateurs/list";
    }

    @PostMapping("/{id}/traiter")
    public String traiter(@PathVariable Long id, @RequestParam String statut,
                           @RequestParam(required = false) String reponse,
                           @AuthenticationPrincipal User admin) {
        affectationFormateurService.traiter(id, statut, reponse, admin);
        return "redirect:/backoffice/affectations-formateurs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        affectationFormateurService.delete(id);
        return "redirect:/backoffice/affectations-formateurs";
    }
}
