package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.NotificationCreateRequest;
import ma.uh1.mpresence.dto.NotificationDto;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.Notification;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final DoctorantRepository doctorantRepository;

    public List<NotificationDto> getAll() {
        return notificationRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Notifications visibles par un doctorant donné : les siennes + les diffusions globales.
     */
    public List<NotificationDto> getForDoctorant(Long doctorantId) {
        return notificationRepository.findByDoctorantIdOrDoctorantIsNull(doctorantId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public NotificationDto create(NotificationCreateRequest request) {
        Notification notification = new Notification();
        notification.setTitre(request.getTitre());
        notification.setMessage(request.getMessage());

        if (request.getDoctorantId() != null) {
            Doctorant doctorant = doctorantRepository.findById(request.getDoctorantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + request.getDoctorantId()));
            notification.setDoctorant(doctorant);
        }

        return toDto(notificationRepository.save(notification));
    }

    public NotificationDto markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable: " + id));
        notification.setLu(true);
        return toDto(notificationRepository.save(notification));
    }

    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification introuvable: " + id);
        }
        notificationRepository.deleteById(id);
    }

    private NotificationDto toDto(Notification n) {
        return new NotificationDto(
                n.getId(), n.getTitre(), n.getMessage(), n.getDateCreation(),
                n.getDoctorant() != null ? n.getDoctorant().getId() : null,
                n.getDoctorant() != null ? n.getDoctorant().getNom() : null,
                n.isLu()
        );
    }
}
