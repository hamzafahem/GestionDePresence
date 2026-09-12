package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DemandeCreateRequest;
import ma.uh1.mpresence.dto.DemandeTraitementRequest;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.DemandeService;
import ma.uh1.mpresence.service.DoctorantService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/demandes")
@RequiredArgsConstructor
public class DemandeWebController {

    private final DemandeService demandeService;
    private final DoctorantService doctorantService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("demandes", demandeService.getAll());
        return "backoffice/demandes/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new DemandeCreateRequest());
        model.addAttribute("doctorants", doctorantService.getAll());
        return "backoffice/demandes/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") DemandeCreateRequest request) {
        demandeService.create(request);
        return "redirect:/backoffice/demandes";
    }

    @PostMapping("/{id}/traiter")
    public String traiter(@PathVariable Long id, @ModelAttribute DemandeTraitementRequest request,
                           @AuthenticationPrincipal User currentUser) {
        demandeService.traiter(id, request, currentUser);
        return "redirect:/backoffice/demandes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        demandeService.delete(id);
        return "redirect:/backoffice/demandes";
    }
}
