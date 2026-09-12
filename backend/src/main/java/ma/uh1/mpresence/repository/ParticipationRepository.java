package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findFirstByDoctorantIdAndModuleIdAndDateParticipationBetween(
            Long doctorantId, Long moduleId, LocalDateTime start, LocalDateTime end);
    List<Participation> findByModuleId(Long moduleId);
    List<Participation> findByDoctorantId(Long doctorantId);
    List<Participation> findByDoctorantIdOrderByDateParticipationDesc(Long doctorantId);
    List<Participation> findByDateParticipationAfter(LocalDateTime after);
}
