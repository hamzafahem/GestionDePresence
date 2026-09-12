package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.Reinscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReinscriptionRepository extends JpaRepository<Reinscription, Long> {
    List<Reinscription> findByDoctorantId(Long doctorantId);
    List<Reinscription> findByDateCreationAfter(LocalDateTime after);
}
