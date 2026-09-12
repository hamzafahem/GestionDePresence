package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.FormateurDto;
import ma.uh1.mpresence.dto.FormateurUpsertRequest;
import ma.uh1.mpresence.service.FormateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/formateurs")
@RequiredArgsConstructor
public class FormateurWebController {

    private final FormateurService formateurService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("formateurs", formateurService.getAll());
        return "backoffice/formateurs/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new FormateurUpsertRequest());
        return "backoffice/formateurs/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") FormateurUpsertRequest request) {
        formateurService.create(request);
        return "redirect:/backoffice/formateurs";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("formateurId", id);
        model.addAttribute("request", toUpsertRequest(formateurService.getOne(id)));
        return "backoffice/formateurs/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("request") FormateurUpsertRequest request) {
        formateurService.update(id, request);
        return "redirect:/backoffice/formateurs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        formateurService.delete(id);
        return "redirect:/backoffice/formateurs";
    }

    private FormateurUpsertRequest toUpsertRequest(FormateurDto dto) {
        FormateurUpsertRequest request = new FormateurUpsertRequest();
        request.setNomFr(dto.getNomFr());
        request.setPrenomFr(dto.getPrenomFr());
        request.setCin(dto.getCin());
        request.setEmail(dto.getEmail());
        request.setPhone(dto.getPhone());
        request.setAge(dto.getAge());
        request.setSexe(dto.getSexe());
        request.setAddress(dto.getAddress());
        return request;
    }
}
