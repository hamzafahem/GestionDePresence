package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ReinscriptionCreateRequest;
import ma.uh1.mpresence.dto.ReinscriptionTraitementRequest;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.repository.AnneeUniversitaireRepository;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.ReinscriptionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/reinscriptions")
@RequiredArgsConstructor
public class ReinscriptionWebController {

    private final ReinscriptionService reinscriptionService;
    private final DoctorantService doctorantService;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reinscriptions", reinscriptionService.getAll());
        return "backoffice/reinscriptions/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new ReinscriptionCreateRequest());
        model.addAttribute("doctorants", doctorantService.getAll());
        model.addAttribute("annees", anneeUniversitaireRepository.findAll());
        return "backoffice/reinscriptions/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") ReinscriptionCreateRequest request) {
        reinscriptionService.create(request);
        return "redirect:/backoffice/reinscriptions";
    }

    @PostMapping("/{id}/traiter")
    public String traiter(@PathVariable Long id, @ModelAttribute ReinscriptionTraitementRequest request,
                           @AuthenticationPrincipal User currentUser) {
        reinscriptionService.traiter(id, request, currentUser);
        return "redirect:/backoffice/reinscriptions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        reinscriptionService.delete(id);
        return "redirect:/backoffice/reinscriptions";
    }
}
