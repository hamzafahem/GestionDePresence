package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ActivityItemDto;
import ma.uh1.mpresence.dto.DashboardStatsDto;
import ma.uh1.mpresence.dto.WeeklyChartDto;
import ma.uh1.mpresence.entity.*;
import ma.uh1.mpresence.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final DateTimeFormatter DAY_LABEL = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH);

    private final FormationRepository formationRepository;
    private final DoctorantRepository doctorantRepository;
    private final RendezVousRepository rendezVousRepository;
    private final DemandeRepository demandeRepository;
    private final ReinscriptionRepository reinscriptionRepository;
    private final ParticipationRepository participationRepository;
    private final PublicationRechercheRepository publicationRepository;
    private final NotificationRepository notificationRepository;

    public DashboardStatsDto getStats() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

        return new DashboardStatsDto(
                formationRepository.count(), formationRepository.countByDateCreationAfter(weekAgo),
                doctorantRepository.count(), doctorantRepository.countByDateCreationAfter(weekAgo),
                rendezVousRepository.count(), rendezVousRepository.findByDateCreationAfter(weekAgo).size(),
                demandeRepository.count(), demandeRepository.findByDateCreationAfter(weekAgo).size()
        );
    }

    public WeeklyChartDto getWeeklyActivity() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        LocalDateTime startOfRange = start.atStartOfDay();

        List<LocalDateTime> allEvents = new ArrayList<>();
        allEvents.addAll(demandeRepository.findByDateCreationAfter(startOfRange).stream().map(Demande::getDateCreation).toList());
        allEvents.addAll(reinscriptionRepository.findByDateCreationAfter(startOfRange).stream().map(Reinscription::getDateCreation).toList());
        allEvents.addAll(participationRepository.findByDateParticipationAfter(startOfRange).stream().map(Participation::getDateParticipation).toList());
        allEvents.addAll(rendezVousRepository.findByDateCreationAfter(startOfRange).stream().map(RendezVous::getDateCreation).toList());
        allEvents.addAll(publicationRepository.findByDateCreationAfter(startOfRange).stream().map(PublicationRecherche::getDateCreation).toList());

        List<String> labels = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate day = start.plusDays(i);
            long count = allEvents.stream().filter(dt -> dt.toLocalDate().equals(day)).count();
            labels.add(capitalize(day.format(DAY_LABEL)));
            counts.add((int) count);
        }

        int max = counts.stream().mapToInt(Integer::intValue).max().orElse(0);
        int chartMax = Math.max(max, 1);

        StringBuilder points = new StringBuilder();
        for (int i = 0; i < counts.size(); i++) {
            double x = counts.size() == 1 ? 0 : (700.0 * i / (counts.size() - 1));
            double y = 190 - (170.0 * counts.get(i) / chartMax);
            if (i > 0) points.append(' ');
            points.append(String.format(Locale.US, "%.1f,%.1f", x, y));
        }

        String rangeLabel = capitalize(start.format(DAY_LABEL)) + " – " + capitalize(today.format(DAY_LABEL));

        return new WeeklyChartDto(labels, counts, points.toString(), max, rangeLabel);
    }

    public List<ActivityItemDto> getRecentActivity() {
        Stream<ActivityItemDto> demandes = demandeRepository.findTop5ByOrderByDateCreationDesc().stream()
                .map(d -> new ActivityItemDto(
                        "bi-envelope-paper-fill", "text-primary bg-primary-subtle",
                        "Nouvelle demande",
                        d.getDoctorant().getNom() + " " + d.getDoctorant().getPrenom() + " — " + d.getObjet(),
                        d.getDateCreation()
                ));

        Stream<ActivityItemDto> rdvs = rendezVousRepository.findTop5ByOrderByDateCreationDesc().stream()
                .map(r -> new ActivityItemDto(
                        "bi-calendar-event-fill", "text-success bg-success-subtle",
                        "Rendez-vous " + r.getStatut().name().toLowerCase(Locale.FRENCH),
                        r.getObjet() + " — " + r.getDoctorant().getNom() + " " + r.getDoctorant().getPrenom(),
                        r.getDateCreation()
                ));

        Stream<ActivityItemDto> publications = publicationRepository.findTop5ByOrderByDateCreationDesc().stream()
                .map(p -> new ActivityItemDto(
                        "bi-journal-richtext", "text-warning bg-warning-subtle",
                        "Publication ajoutée",
                        p.getTitre(),
                        p.getDateCreation()
                ));

        Stream<ActivityItemDto> notifications = notificationRepository.findTop5ByOrderByDateCreationDesc().stream()
                .map(n -> new ActivityItemDto(
                        "bi-bell-fill", "text-danger bg-danger-subtle",
                        n.getTitre(),
                        n.getMessage(),
                        n.getDateCreation()
                ));

        return Stream.of(demandes, rdvs, publications, notifications)
                .flatMap(s -> s)
                .sorted(Comparator.comparing(ActivityItemDto::getDateCreation).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
