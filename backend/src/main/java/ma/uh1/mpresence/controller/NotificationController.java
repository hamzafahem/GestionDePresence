package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.NotificationCreateRequest;
import ma.uh1.mpresence.dto.NotificationDto;
import ma.uh1.mpresence.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDto> getAll() {
        return notificationService.getAll();
    }

    @GetMapping("/doctorant/{doctorantId}")
    public List<NotificationDto> getForDoctorant(@PathVariable Long doctorantId) {
        return notificationService.getForDoctorant(doctorantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDto create(@Valid @RequestBody NotificationCreateRequest request) {
        return notificationService.create(request);
    }

    @PostMapping("/{id}/lu")
    public NotificationDto markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        notificationService.delete(id);
    }
}
