package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.Demande;
import ma.uh1.mpresence.entity.StatutWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByDoctorantId(Long doctorantId);
    long countByStatut(StatutWorkflow statut);
    List<Demande> findByDateCreationAfter(LocalDateTime after);
    List<Demande> findTop5ByOrderByDateCreationDesc();
    List<Demande> findByModuleIdAndStatut(Long moduleId, StatutWorkflow statut);
}
