package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface FormationRepository extends JpaRepository<Formation, Long> {
    long countByDateCreationAfter(LocalDateTime after);
    long countByFormateurId(Long formateurId);
}
