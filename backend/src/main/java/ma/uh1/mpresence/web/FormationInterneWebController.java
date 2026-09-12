package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.FormationInterneUpsertRequest;
import ma.uh1.mpresence.repository.UserRepository;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.FormationInterneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/formations-internes")
@RequiredArgsConstructor
public class FormationInterneWebController {

    private final FormationInterneService formationInterneService;
    private final DoctorantService doctorantService;
    private final UserRepository userRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("formations", formationInterneService.getAll());
        return "backoffice/formations-internes/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new FormationInterneUpsertRequest());
        model.addAttribute("formateurs", userRepository.findAll());
        return "backoffice/formations-internes/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") FormationInterneUpsertRequest request) {
        formationInterneService.create(request);
        return "redirect:/backoffice/formations-internes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        formationInterneService.delete(id);
        return "redirect:/backoffice/formations-internes";
    }

    @GetMapping("/{id}/participants")
    public String participants(@PathVariable Long id, Model model) {
        model.addAttribute("formation", formationInterneService.getOne(id));
        model.addAttribute("participants", formationInterneService.getParticipants(id));
        model.addAttribute("doctorants", doctorantService.getAll());
        return "backoffice/formations-internes/participants";
    }

    @PostMapping("/{id}/inscrire")
    public String inscrire(@PathVariable Long id, @RequestParam Long doctorantId) {
        formationInterneService.inscrire(id, doctorantId);
        return "redirect:/backoffice/formations-internes/" + id + "/participants";
    }

    @PostMapping("/{id}/participants/{doctorantId}/delete")
    public String desinscrire(@PathVariable Long id, @PathVariable Long doctorantId) {
        formationInterneService.desinscrire(id, doctorantId);
        return "redirect:/backoffice/formations-internes/" + id + "/participants";
    }
}
