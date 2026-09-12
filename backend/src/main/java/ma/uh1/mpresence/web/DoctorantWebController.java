package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DoctorantDto;
import ma.uh1.mpresence.dto.DoctorantUpsertRequest;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.UserRepository;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.FormationService;
import ma.uh1.mpresence.service.ParticipationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backoffice/doctorants")
@RequiredArgsConstructor
public class DoctorantWebController {

    private final DoctorantService doctorantService;
    private final FormationService formationService;
    private final UserRepository userRepository;
    private final DoctorantRepository doctorantRepository;
    private final ParticipationService participationService;

    @GetMapping
    public String list(Model model) {
        var doctorants = doctorantService.getAll();
        model.addAttribute("doctorants", doctorants);
        model.addAttribute("filieres", doctorants.stream()
                .map(ma.uh1.mpresence.dto.DoctorantDto::getFiliere)
                .filter(f -> f != null && !f.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
        model.addAttribute("formateurs", userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("FORMATEUR")))
                .collect(Collectors.toList()));
        return "backoffice/doctorants/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        DoctorantDto doctorant = doctorantService.getOne(id);
        List<DoctorantDto> all = doctorantService.getAll();

        int index = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }
        Long previousId = index > 0 ? all.get(index - 1).getId() : null;
        Long nextId = index >= 0 && index < all.size() - 1 ? all.get(index + 1).getId() : null;

        Doctorant entity = doctorantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + id));

        model.addAttribute("doctorant", doctorant);
        model.addAttribute("previousId", previousId);
        model.addAttribute("nextId", nextId);
        model.addAttribute("historique", participationService.getMesParticipations(entity));
        return "backoffice/doctorants/view";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new DoctorantUpsertRequest());
        model.addAttribute("formations", formationService.getAll());
        return "backoffice/doctorants/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") DoctorantUpsertRequest request) {
        doctorantService.create(request);
        return "redirect:/backoffice/doctorants";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("doctorantId", id);
        model.addAttribute("request", toUpsertRequest(doctorantService.getOne(id)));
        model.addAttribute("formations", formationService.getAll());
        return "backoffice/doctorants/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("request") DoctorantUpsertRequest request) {
        doctorantService.update(id, request);
        return "redirect:/backoffice/doctorants";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        doctorantService.delete(id);
        return "redirect:/backoffice/doctorants";
    }

    @PostMapping("/{id}/encadrant")
    public String affecterEncadrant(@PathVariable Long id, @RequestParam Long formateurId) {
        doctorantService.affecterEncadrant(id, formateurId);
        return "redirect:/backoffice/doctorants";
    }

    @PostMapping("/{id}/encadrant/delete")
    public String retirerEncadrant(@PathVariable Long id) {
        doctorantService.retirerEncadrant(id);
        return "redirect:/backoffice/doctorants";
    }

    private DoctorantUpsertRequest toUpsertRequest(ma.uh1.mpresence.dto.DoctorantDto dto) {
        DoctorantUpsertRequest request = new DoctorantUpsertRequest();
        request.setNom(dto.getNom());
        request.setPrenom(dto.getPrenom());
        request.setEmail(dto.getEmail());
        request.setMobile(dto.getMobile());
        request.setCin(dto.getCin());
        request.setCne(dto.getCne());
        request.setCodeApogee(dto.getCodeApogee());
        request.setFiliere(dto.getFiliere());
        request.setNationalite(dto.getNationalite());
        request.setPhoto(dto.getPhoto());
        request.setDateNaissance(dto.getDateNaissance());
        request.setDetails(dto.getDetails());
        request.setFormationId(dto.getFormationId());
        request.setStatut(dto.getStatut());
        return request;
    }
}
