package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.NotificationCreateRequest;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/backoffice/notifications")
@RequiredArgsConstructor
public class NotificationWebController {

    private final NotificationService notificationService;
    private final DoctorantService doctorantService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notifications", notificationService.getAll());
        return "backoffice/notifications/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("request", new NotificationCreateRequest());
        model.addAttribute("doctorants", doctorantService.getAll());
        return "backoffice/notifications/form";
    }

    @PostMapping
    public String create(@ModelAttribute("request") NotificationCreateRequest request) {
        notificationService.create(request);
        return "redirect:/backoffice/notifications";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        notificationService.delete(id);
        return "redirect:/backoffice/notifications";
    }
}
