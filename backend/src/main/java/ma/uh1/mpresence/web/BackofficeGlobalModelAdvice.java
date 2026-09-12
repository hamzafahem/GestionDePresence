package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.entity.StatutWorkflow;
import ma.uh1.mpresence.repository.DemandeAffectationFormateurRepository;
import ma.uh1.mpresence.repository.DemandeRepository;
import ma.uh1.mpresence.repository.InscriptionRepository;
import ma.uh1.mpresence.repository.NotificationRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Rend les badges de la sidebar/topbar (notifications non lues, demandes en
 * attente) disponibles dans TOUTES les pages du back-office, sans avoir à
 * les ajouter manuellement dans chaque contrôleur.
 */
@ControllerAdvice(basePackages = "ma.uh1.mpresence.web")
@RequiredArgsConstructor
public class BackofficeGlobalModelAdvice {

    private final DemandeRepository demandeRepository;
    private final NotificationRepository notificationRepository;
    private final DemandeAffectationFormateurRepository demandeAffectationFormateurRepository;
    private final InscriptionRepository inscriptionRepository;

    @ModelAttribute("pendingDemandesCount")
    public long pendingDemandesCount() {
        return demandeRepository.countByStatut(StatutWorkflow.EN_ATTENTE);
    }

    @ModelAttribute("unreadNotificationsCount")
    public long unreadNotificationsCount() {
        return notificationRepository.countByLuFalse();
    }

    @ModelAttribute("pendingAffectationsFormateurCount")
    public long pendingAffectationsFormateurCount() {
        return demandeAffectationFormateurRepository.countByStatut(StatutWorkflow.EN_ATTENTE);
    }

    @ModelAttribute("pendingInscriptionsCount")
    public long pendingInscriptionsCount() {
        return inscriptionRepository.countByStatut(StatutWorkflow.EN_ATTENTE);
    }
}
