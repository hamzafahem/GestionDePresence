package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByDoctorantId(Long doctorantId);
    List<Inscription> findByFormationId(Long formationId);
    Optional<Inscription> findByDoctorantIdAndFormationIdAndStatut(Long doctorantId, Long formationId, ma.uh1.mpresence.entity.StatutWorkflow statut);
    long countByStatut(ma.uh1.mpresence.entity.StatutWorkflow statut);
}
