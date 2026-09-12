package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.AnneeUniversitaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnneeUniversitaireRepository extends JpaRepository<AnneeUniversitaire, Long> {
    Optional<AnneeUniversitaire> findByActiveTrue();
}
