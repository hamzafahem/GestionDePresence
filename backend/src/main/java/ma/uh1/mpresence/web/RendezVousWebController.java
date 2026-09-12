package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.RendezVousUpsertRequest;
import ma.uh1.mpresence.entity.StatutRendezVous;
import ma.uh1.mpresence.repository.UserRepository;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.RendezVousService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/rendezvous")
@RequiredArgsConstructor
public class RendezVousWebController {

    private final RendezVousService rendezVousService;
    private final DoctorantService doctorantService;
    private final UserRepository userRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rendezvous", rendezVousService.getAll());
        return "backoffice/rendezvous/list";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) Long doctorantId, Model model) {
        RendezVousUpsertRequest request = new RendezVousUpsertRequest();
        request.setDoctorantId(doctorantId);
        model.addAttribute("request", request);
        model.addAttribute("doctorants", doctorantService.getAll());
        model.addAttribute("formateurs", userRepository.findAll());
        model.addAttribute("statuts", StatutRendezVous.values());
        return "backoffice/rendezvous/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") RendezVousUpsertRequest request) {
        rendezVousService.create(request);
        return "redirect:/backoffice/rendezvous";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        rendezVousService.delete(id);
        return "redirect:/backoffice/rendezvous";
    }
}
