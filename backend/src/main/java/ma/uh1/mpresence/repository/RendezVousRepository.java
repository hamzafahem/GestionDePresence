package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByDoctorantId(Long doctorantId);
    List<RendezVous> findByDateCreationAfter(LocalDateTime after);
    List<RendezVous> findTop5ByOrderByDateCreationDesc();
    long countByDateHeureBetween(LocalDateTime start, LocalDateTime end);
}
