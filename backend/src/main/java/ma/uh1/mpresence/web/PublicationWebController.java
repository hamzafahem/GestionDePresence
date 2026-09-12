package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.PublicationUpsertRequest;
import ma.uh1.mpresence.entity.TypePublication;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.PublicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/publications")
@RequiredArgsConstructor
public class PublicationWebController {

    private final PublicationService publicationService;
    private final DoctorantService doctorantService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("publications", publicationService.getAll());
        return "backoffice/publications/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new PublicationUpsertRequest());
        model.addAttribute("doctorants", doctorantService.getAll());
        model.addAttribute("types", TypePublication.values());
        return "backoffice/publications/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") PublicationUpsertRequest request) {
        publicationService.create(request);
        return "redirect:/backoffice/publications";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        publicationService.delete(id);
        return "redirect:/backoffice/publications";
    }
}
