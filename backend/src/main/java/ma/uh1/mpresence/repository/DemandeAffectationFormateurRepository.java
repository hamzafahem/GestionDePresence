package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.DemandeAffectationFormateur;
import ma.uh1.mpresence.entity.StatutWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeAffectationFormateurRepository extends JpaRepository<DemandeAffectationFormateur, Long> {
    List<DemandeAffectationFormateur> findByFormateurIdOrderByDateCreationDesc(Long formateurId);
    List<DemandeAffectationFormateur> findByStatutOrderByDateCreationDesc(StatutWorkflow statut);
    long countByStatut(StatutWorkflow statut);
}
