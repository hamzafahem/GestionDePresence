package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.FormationUpsertRequest;
import ma.uh1.mpresence.dto.ModuleUpsertRequest;
import ma.uh1.mpresence.service.FormateurService;
import ma.uh1.mpresence.service.FormationService;
import ma.uh1.mpresence.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/formations")
@RequiredArgsConstructor
public class FormationWebController {

    private final FormationService formationService;
    private final ModuleService moduleService;
    private final FormateurService formateurService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("formations", formationService.getAll());
        model.addAttribute("formateurs", formateurService.getAll());
        return "backoffice/formations/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new FormationUpsertRequest());
        model.addAttribute("formateurs", formateurService.getAll());
        return "backoffice/formations/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") FormationUpsertRequest request) {
        formationService.create(request);
        return "redirect:/backoffice/formations";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("formationId", id);
        model.addAttribute("request", toUpsertRequest(formationService.getOne(id)));
        model.addAttribute("formateurs", formateurService.getAll());
        return "backoffice/formations/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("request") FormationUpsertRequest request) {
        formationService.update(id, request);
        return "redirect:/backoffice/formations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        formationService.delete(id);
        return "redirect:/backoffice/formations";
    }

    @PostMapping("/{id}/formateur")
    public String affecterFormateur(@PathVariable Long id, @RequestParam(required = false) Long formateurId) {
        formationService.affecterFormateur(id, formateurId);
        return "redirect:/backoffice/formations";
    }

    @GetMapping("/{id}/modules")
    public String modules(@PathVariable Long id, Model model) {
        model.addAttribute("formation", formationService.getOne(id));
        model.addAttribute("modules", moduleService.getAllForFormation(id));
        model.addAttribute("formateurs", formateurService.getAll());
        model.addAttribute("request", new ModuleUpsertRequest());
        return "backoffice/formations/modules";
    }

    @PostMapping("/{id}/modules")
    public String addModule(@PathVariable Long id, @ModelAttribute("request") ModuleUpsertRequest request) {
        moduleService.create(id, request);
        return "redirect:/backoffice/formations/" + id + "/modules";
    }

    @PostMapping("/{id}/modules/{moduleId}/formateur")
    public String affecterFormateurModule(@PathVariable Long id, @PathVariable Long moduleId,
                                           @RequestParam(required = false) Long formateurId) {
        moduleService.affecterFormateur(id, moduleId, formateurId);
        return "redirect:/backoffice/formations/" + id + "/modules";
    }

    @PostMapping("/{id}/modules/{moduleId}/delete")
    public String deleteModule(@PathVariable Long id, @PathVariable Long moduleId) {
        moduleService.delete(id, moduleId);
        return "redirect:/backoffice/formations/" + id + "/modules";
    }

    private FormationUpsertRequest toUpsertRequest(ma.uh1.mpresence.dto.FormationDto dto) {
        FormationUpsertRequest request = new FormationUpsertRequest();
        request.setIntitule(dto.getIntitule());
        request.setDescription(dto.getDescription());
        request.setDateDebut(dto.getDateDebut());
        request.setDateFin(dto.getDateFin());
        request.setFormateurId(dto.getFormateurId());
        return request;
    }
}
