package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.Doctorant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoctorantRepository extends JpaRepository<Doctorant, Long> {
    Optional<Doctorant> findByCodeApogeeIgnoreCase(String codeApogee);
    long countByDateCreationAfter(LocalDateTime after);
    List<Doctorant> findByFormationId(Long formationId);
}
